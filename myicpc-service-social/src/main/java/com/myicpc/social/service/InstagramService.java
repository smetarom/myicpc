package com.myicpc.social.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.myicpc.commons.adapters.JSONAdapter;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.commons.utils.WebServiceUtils;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.BlacklistedUser;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.service.notification.NotificationBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class provides services for managing Instagram
 *
 * @author Roman Smetana
 */
@Service
@Transactional
public class InstagramService extends ASocialService {
    private static final Logger logger = LoggerFactory.getLogger(InstagramService.class);
    private static final String INSTAGRAM_RECENT_TAG_URL = "https://api.instagram.com/v1/tags/%s/media/recent";
    private static final String INSTAGRAM_CHECK_URL = "https://api.instagram.com/v1/tags/icpc";
    private static final Integer INSTAGRAM_MAX_PAGES = 3;

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Checks that Instagram API keys are valid and MyICPC can use them
     *
     * @param instagramClientId Instagram client ID key
     * @return are API keys valid
     */
    public boolean checkInstagramConfiguration(String instagramClientId) {
        HttpGet httpGet = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("client_id", instagramClientId));
            String paramsString = URLEncodedUtils.format(params, FormatUtils.DEFAULT_ENCODING);
            String url = INSTAGRAM_CHECK_URL + "?" + paramsString;
            httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpclient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            String response = IOUtils.toString(entity.getContent(), FormatUtils.DEFAULT_ENCODING);
            JsonObject root = new JsonParser().parse(response).getAsJsonObject();
            int code = root.getAsJsonObject("meta").getAsJsonPrimitive("code").getAsInt();
            return code == 200;
        } catch (IOException ex) {
            return false;
        } finally {
            WebServiceUtils.releaseConnection(httpGet);
        }
    }

    /**
     * Starts publish subscribe Instagram service
     *
     * It registers {@code callbackUrl} on Instagram and receives notifications
     * about new posts on that URL
     *
     * @param contest contest
     * @param callbackUrl notification receive URL
     * @throws URISyntaxException wrong URL format
     * @throws IOException Instagram communication error
     */
    public void startSubscription(final Contest contest, String callbackUrl) throws URISyntaxException, IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            URI uri = new URIBuilder().setScheme("https")
                    .setHost("api.instagram.com")
                    .setPath("/v1/subscriptions/")
                    .build();
            List<NameValuePair> formparams = new ArrayList<>();
            formparams.add(new BasicNameValuePair("client_id", contest.getWebServiceSettings().getInstagramKey()));
            formparams.add(new BasicNameValuePair("client_secret", contest.getWebServiceSettings().getInstagramSecret()));
            formparams.add(new BasicNameValuePair("object", "tag"));
            formparams.add(new BasicNameValuePair("aspect", "media"));
            formparams.add(new BasicNameValuePair("object_id", contest.getHashtag()));
            formparams.add(new BasicNameValuePair("callback_url", callbackUrl));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(entity);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("Instagram subscription of tag {} was created", contest.getHashtag());
            } else {
                logger.error("Instagram subscription of tag {} failed. Reason: {}", contest.getHashtag(), response.getStatusLine().getReasonPhrase());
            }
            response.close();
        }
    }

    /**
     * Receives new Instagram posts for contest
     *
     * @param contest contest
     * @throws WebServiceException communication with Instagram failed
     */
    public void getNewPosts(final Contest contest) throws WebServiceException {
        if (StringUtils.isEmpty(contest.getWebServiceSettings().getInstagramKey())) {
            return;
        }

        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("client_id", contest.getWebServiceSettings().getInstagramKey()));
        String paramsString = URLEncodedUtils.format(params, FormatUtils.DEFAULT_ENCODING);
        String url = String.format(INSTAGRAM_RECENT_TAG_URL, contest.getHashtag()) + "?" + paramsString;
        List<Notification> notifications = getByHashTag(contest, url, 1);

        saveSearchList(notifications, BlacklistedUser.BlacklistedUserType.INSTAGRAM, contest);
    }

    private List<Notification> getByHashTag(final Contest contest, String url, int page) throws WebServiceException {
        HttpGet httpGet = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            httpGet = new HttpGet(url);

            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            return parseInstagramResponse(contest, IOUtils.toString(entity.getContent(), FormatUtils.DEFAULT_ENCODING), page);
        } catch (IOException ex) {
            throw new WebServiceException(ex);
        } finally {
            WebServiceUtils.releaseConnection(httpGet);
        }
    }

    private List<Notification> parseInstagramResponse(final Contest contest, String json, int page) throws WebServiceException {
        if (page > INSTAGRAM_MAX_PAGES) {
            return new ArrayList<>();
        }
        List<Notification> list = new ArrayList<>();

        try {
            JsonObject root = new JsonParser().parse(json).getAsJsonObject();
            JsonArray data = root.getAsJsonArray("data");
            if (data != null) {
                // process each received JSON record
                for (JsonElement jsonElement : data) {
                    JSONAdapter mediaAdapter = new JSONAdapter(jsonElement);
                    String id = mediaAdapter.getString("id");

                    if (notificationRepository.countByContestAndExternalIdAndNotificationType(contest, id, NotificationType.INSTAGRAM) > 0) {
                        return list;
                    }
                    String mediaType = mediaAdapter.getString("type");
                    NotificationBuilder builder = new NotificationBuilder();
                    builder.setNotificationType(NotificationType.INSTAGRAM);
                    builder.setContest(contest);

                    builder.setExternalId(id);
                    builder.setUrl(mediaAdapter.getString("link"));

                    JSONAdapter userAdapter = new JSONAdapter(mediaAdapter.get("user"));
                    String fullname = userAdapter.getString("full_name");
                    String username = userAdapter.getString("username");
                    builder.setAuthorName(StringUtils.isEmpty(fullname) ? username : fullname);
                    builder.setAuthorUsername(username);
                    builder.setProfilePictureUrl(userAdapter.getString("profile_picture"));
                    builder.setTimestamp(new Date(mediaAdapter.getLong("created_time") * 1000));
                    builder.setTitle(username);
                    builder.setBody(mediaAdapter.getStringFromObject("caption", "text"));
                    if ("image".equalsIgnoreCase(mediaType)) {
                        builder.setImageUrl(mediaAdapter.getStringFromObject("images", "standard_resolution", "url"));
                    } else if ("video".equalsIgnoreCase(mediaType)) {
                        builder.setVideoUrl(mediaAdapter.getStringFromObject("videos", "standard_resolution", "url"));
                    }
                    builder.setThumbnailUrl(mediaAdapter.getStringFromObject("images", "low_resolution", "url"));

                    String[] tags = mediaAdapter.getJsonArrayValues("tags");
                    builder.setHashtags(createHashtags(tags, contest.getHashtag(), contest.getQuestConfiguration().getHashtagPrefix()));

                    list.add(builder.build());
                }

                // if the result has next page
                if (root.get("pagination").getAsJsonObject().get("next_url") != null) {
                    List<Notification> nextPage = getByHashTag(contest, root.get("pagination").getAsJsonObject().get("next_url").getAsString(), page + 1);
                    if (nextPage != null && !nextPage.isEmpty()) {
                        list.addAll(nextPage);
                    }
                }
            }
        } catch (JsonParseException ex) {
            throw new WebServiceException("Error parsing JSON: " + json, ex);
        }

        return list;
    }
}
