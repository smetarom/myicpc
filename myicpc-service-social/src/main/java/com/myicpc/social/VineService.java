package com.myicpc.social;

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
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.service.notification.NotificationBuilder;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class VineService extends ASocialService {
    private static final int VINE_MAX_PAGES = 1;
    private static final String VINE_TAG_URL = "https://api.vineapp.com/timelines/tags/%s";

    /**
     * Login a user into Vine
     *
     * @param httpClient
     *            http client
     * @return get login session key
     * @throws WebServiceException
     *             web communication with Vine failed
     */
    protected String login(final HttpClient httpClient, final Contest contest) throws WebServiceException {
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost("https://api.vineapp.com/users/authenticate");
            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("username", contest.getWebServiceSettings().getVineEmail()));
            params.add(new BasicNameValuePair("password", contest.getWebServiceSettings().getVinePassword()));
            httpPost.setEntity(new UrlEncodedFormEntity(params, FormatUtils.DEFAULT_ENCODING));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            JsonObject root = new JsonParser().parse(IOUtils.toString(entity.getContent(), FormatUtils.DEFAULT_ENCODING)).getAsJsonObject();
            JSONAdapter rootAdapter = new JSONAdapter(root);
            if (!rootAdapter.getBoolean("success") || !rootAdapter.has("data")) {
                throw new WebServiceException("Unsuccessful Vine login.");
            }
            return rootAdapter.getStringFromObject("data", "key");
        } catch (Throwable ex) {
            throw new WebServiceException(ex);
        } finally {
            WebServiceUtils.releaseConnection(httpPost);
        }
    }

    /**
     * Receive new Vine posts for given hashtag
     *
     * @param hashTag
     *            hashtag to be searched
     * @throws WebServiceException
     *             communication with Vine failed
     */
    public void getNewPosts(final Contest contest) throws WebServiceException {
        if (StringUtils.isEmpty(contest.getWebServiceSettings().getVineEmail()) || StringUtils.isEmpty(contest.getWebServiceSettings().getVinePassword())) {
            return;
        }

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String vineApiKey = login(httpclient, contest);
            if (vineApiKey == null) {
                throw new WebServiceException("Unsuccessful Vine login.");
            }
            List<Notification> notifications = getByHashTag(httpclient, contest, vineApiKey, 1);
            saveSearchList(notifications, BlacklistedUser.BlacklistedUserType.VINE, contest);
        } catch (IOException ex) {
            throw new WebServiceException(ex);
        }
    }

    /**
     * Gets all Vine posts till the post last seen or the search limit is
     * reached
     *
     * @param hashTag
     *            hashtag to be searched
     * @param vineApiKey
     *@param page
     *            number of search page  @return Vine posts for hashtag
     * @throws WebServiceException
     *             communication with Vine failed
     */
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

    /**
     * Parses returned JSON and creates {@link com.myicpc.model.social.Notification} entries
     *
     * @param json
     *            recieved JSON from Vine web service
     * @param hashTag
     *            hashtag to be searched
     * @return Vine posts for hashtag
     * @throws WebServiceException
     *             communication with Vine failed
     */
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
                Notification notification = notificationRepository.findByContestAndExternalIdAndNotificationType(contest, mediaId, NotificationType.VINE);
                if (notification != null) {
                    return list; // Already parsed before
                }
                NotificationBuilder builder = new NotificationBuilder();
                builder.setExternalId(mediaId);
                builder.setNotificationType(NotificationType.VINE);
                builder.setTitle(mediaAdapter.getString("userId"));
                builder.setBody(mediaAdapter.getString("description"));
                builder.setAuthorName(mediaAdapter.getString("username"));
                builder.setProfilePictureUrl(mediaAdapter.getString("avatarUrl"));
                builder.setUrl(mediaAdapter.getString("permalinkUrl"));
                builder.setVideoUrl(mediaAdapter.getString("videoUrl"));
                builder.setThumbnailUrl(mediaAdapter.getString("thumbnailUrl"));
                DateTimeFormatter fmt = ISODateTimeFormat.dateTimeParser();
                builder.setTimestamp(new Date(fmt.parseMillis(mediaAdapter.getString("created"))));
                builder.setContest(contest);

                JsonArray tagArray = mediaAdapter.getJsonArray("tags");
                String[] tags = new String[tagArray.size()];
                int i = 0;
                for (JsonElement elem : tagArray) {
                    JSONAdapter tagAdapter = new JSONAdapter(elem);
                    tags[i++] = tagAdapter.getString("tag");
                }
                // TODO complete quest hashtag
                builder.setHashTags(createHashtags(tags, contest.getHashtag(), null));

                list.add(builder.build());
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
