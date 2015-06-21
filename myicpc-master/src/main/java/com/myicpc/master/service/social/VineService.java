package com.myicpc.master.service.social;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.myicpc.commons.adapters.JSONAdapter;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.commons.utils.WebServiceUtils;
import com.myicpc.enums.NotificationType;
import com.myicpc.master.exception.AuthenticationException;
import com.myicpc.master.exception.WebServiceException;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Roman Smetana
 */
@Singleton
public class VineService extends GeneralSocialService {
    private static final Logger logger = LoggerFactory.getLogger(VineService.class);

    private static final int VINE_MAX_PAGES = 1;
    private static final String VINE_AUTHENTICATION_URL = "https://api.vineapp.com/users/authenticate";
    private static final String VINE_TAG_URL = "https://api.vineapp.com/timelines/tags/%s";

    private String lastSeenId;
    private final ConcurrentMap<Long, String> authenticationKeys = new ConcurrentHashMap<>();

    private String authenticate(final HttpClient httpClient,final Contest contest) throws IOException, AuthenticationException {
        logger.info("Vine logging...");
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(VINE_AUTHENTICATION_URL);
            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("username", contest.getWebServiceSettings().getVineEmail()));
            params.add(new BasicNameValuePair("password", contest.getWebServiceSettings().getVinePassword()));
            httpPost.setEntity(new UrlEncodedFormEntity(params, FormatUtils.DEFAULT_ENCODING));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            JsonObject root = new JsonParser().parse(IOUtils.toString(entity.getContent(), FormatUtils.DEFAULT_ENCODING)).getAsJsonObject();
            JSONAdapter rootAdapter = new JSONAdapter(root);
            if (!rootAdapter.getBoolean("success") || !rootAdapter.has("data")) {
                throw new AuthenticationException("Unsuccessful Vine login.");
            }
            return rootAdapter.getStringFromObject("data", "key");
        } finally {
            WebServiceUtils.releaseConnection(httpPost);
        }
    }

    public void getNewPosts(final Contest contest) throws WebServiceException, AuthenticationException {
        if (StringUtils.isEmpty(contest.getWebServiceSettings().getVineEmail()) || StringUtils.isEmpty(contest.getWebServiceSettings().getVinePassword())) {
            return;
        }

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String authenticationKey = authenticationKeys.get(contest.getId());
            if (StringUtils.isEmpty(authenticationKey)) {
                authenticationKey = authenticate(httpclient, contest);
                authenticationKeys.put(contest.getId(), authenticationKey);
            }
            if (authenticationKey == null) {
                throw new AuthenticationException("Unsuccessful Vine login.");
            }
            List<Notification> notifications = getByHashTag(httpclient, contest, authenticationKey, 1);
            // update last seen id
            if (!notifications.isEmpty()) {
                lastSeenId = notifications.get(notifications.size() - 1).getExternalId();
            }
            sendSocialNotifications(notifications);
        } catch (IOException ex) {
            throw new WebServiceException(ex);
        } catch (AuthenticationException ex) {
            authenticationKeys.remove(contest.getId());
            throw ex;
        }
    }

    protected List<Notification> getByHashTag(final HttpClient httpClient, final Contest contest, String vineApiKey, int page) throws WebServiceException {
        if (page > VINE_MAX_PAGES) {
            return new ArrayList<>();
        }

        HttpGet httpGet = null;
        try {
            List<NameValuePair> params = new ArrayList<>(2);
            if (page > 1) {
                params.add(new BasicNameValuePair("page", String.valueOf(page)));
            }
            String paramsString = URLEncodedUtils.format(params, FormatUtils.DEFAULT_ENCODING);
            httpGet = new HttpGet(String.format(VINE_TAG_URL, contest.getHashtag()) + "?" + paramsString);
            httpGet.addHeader("vine-session-id", vineApiKey);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return parseNewVineVideos(httpClient, contest, vineApiKey, IOUtils.toString(entity.getContent(), FormatUtils.DEFAULT_ENCODING));
        } catch (Throwable ex) {
            throw new WebServiceException(ex);
        } finally {
            WebServiceUtils.releaseConnection(httpGet);
        }
    }

    protected List<Notification> parseNewVineVideos(final HttpClient httpClient, final Contest contest, String vineApiKey, String json) throws WebServiceException {
        List<Notification> list = new ArrayList<>();

        try {
            JsonObject root = new JsonParser().parse(json).getAsJsonObject();
            if (!root.get("success").getAsBoolean() || root.get("data") == null) {
                throw new WebServiceException("Unsuccessful hash tag search.");
            }
            JsonArray records = root.get("data").getAsJsonObject().getAsJsonArray("records");
            // process each received JSON record
            for (JsonElement je : records) {
                JSONAdapter mediaAdapter = new JSONAdapter(je);
                String mediaId = mediaAdapter.getString("postId");

                // this post and every following post were already processed
                if (mediaId.equals(lastSeenId)) {
                    return list;
                }

                Notification notification = new Notification();
                notification.setExternalId(mediaId);
                notification.setNotificationType(NotificationType.VINE);
                notification.setTitle(mediaAdapter.getString("userId"));
                notification.setBody(mediaAdapter.getString("description"));
                notification.setAuthorName(mediaAdapter.getString("username"));
                notification.setProfilePictureUrl(mediaAdapter.getString("avatarUrl"));
                notification.setUrl(mediaAdapter.getString("permalinkUrl"));
                notification.setVideoUrl(mediaAdapter.getString("videoUrl"));
                notification.setThumbnailUrl(mediaAdapter.getString("thumbnailUrl"));
                DateTimeFormatter fmt = ISODateTimeFormat.dateTimeParser();
                notification.setTimestamp(new Date(fmt.parseMillis(mediaAdapter.getString("created"))));
                notification.setContest(contest);

                JsonArray tagArray = mediaAdapter.getJsonArray("tags");
                String[] tags = new String[tagArray.size()];
                int i = 0;
                for (JsonElement elem : tagArray) {
                    JSONAdapter tagAdapter = new JSONAdapter(elem);
                    tags[i++] = tagAdapter.getString("tag");
                }
                notification.setHashtags(createHashtags(tags, contest.getHashtag(), null));

                list.add(notification);
            }

            // if the result has next page
            if (!root.get("data").getAsJsonObject().get("nextPage").isJsonNull()
                    && !StringUtils.isEmpty(root.get("data").getAsJsonObject().get("nextPage").getAsString())) {
                List<Notification> nextPage = getByHashTag(httpClient, contest, vineApiKey, root.get("data").getAsJsonObject().get("nextPage").getAsInt());
                if (!nextPage.isEmpty()) {
                    list.addAll(nextPage);
                }
            }
        } catch (JsonParseException ex) {
            throw new WebServiceException("Error parsing JSON: " + json, ex);
        }

        return list;
    }

}
