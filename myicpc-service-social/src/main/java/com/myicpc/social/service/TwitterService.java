package com.myicpc.social.service;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.contest.WebServiceSettings;
import com.myicpc.model.social.BlacklistedUser;
import com.myicpc.model.social.Notification;
import com.myicpc.service.contest.ContestService;
import com.myicpc.service.notification.NotificationBuilder;
import com.myicpc.social.dto.TwitterStreamDTO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.ExtendedMediaEntity;
import twitter4j.FilterQuery;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service responsible for Twitter social resources
 * <p/>
 * It manages the streaming from Twitter to application, and parsing it into
 * application representation
 *
 * @author Roman Smetana
 */
@Service
public class TwitterService extends ASocialService {
    private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);
    private static final String VIDEO_FORMAT = "video/mp4";
    private static final String LINK_TO_TWEET = "https://twitter.com/statuses/%s";

    /**
     * Map between contest ID and {@link TwitterStreamDTO}
     */
    private static final ConcurrentMap<Long, TwitterStreamDTO> streamMapping = new ConcurrentHashMap<>();

    /**
     * Checks if provided twitter keys are valid
     *
     * @param twitterConsumerKey       twitter consumer key
     * @param twitterConsumerSecret    twitter consumer secret
     * @param twitterAccessToken       twitter access token
     * @param twitterAccessTokenSecret twitter access token secret
     * @return twitter configuration is valid
     */
    public boolean checkTwitterConfiguration(String twitterConsumerKey,
                                             String twitterConsumerSecret,
                                             String twitterAccessToken,
                                             String twitterAccessTokenSecret) {
        WebServiceSettings webServiceSettings = new WebServiceSettings();
        webServiceSettings.setTwitterAccessToken(twitterAccessToken);
        webServiceSettings.setTwitterAccessTokenSecret(twitterAccessTokenSecret);
        webServiceSettings.setTwitterConsumerKey(twitterConsumerKey);
        webServiceSettings.setTwitterConsumerSecret(twitterConsumerSecret);

        ConfigurationBuilder cb = createTwitterConfiguration(webServiceSettings);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            twitter.getHomeTimeline(new Paging(1, 1));
            return true;
        } catch (TwitterException e) {
            return false;
        }
    }

    /**
     * Starts streaming from Twitter to application by contest hashtag
     *
     * @param contestId contest ID
     */
    public void startTwitterStreaming(Long contestId) {
        Contest contest = contestRepository.findOne(contestId);
        startTwitterStreaming(contest);
    }

    /**
     * Starts streaming from Twitter to application by contest hashtag
     *
     * @param contest contest
     */
    public void startTwitterStreaming(Contest contest) {
        TwitterStream twitterStream = null;
        try {
            ConfigurationBuilder cb = createTwitterConfiguration(contest.getWebServiceSettings());
            twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
            twitterStream.addListener(new TwitterStatusListener(contest));
            twitterStream.filter(new FilterQuery(0, null, new String[]{"#" + contest.getHashtag()}));
        } catch (IllegalArgumentException ex) {
            logger.warn("Contest " + contest.getCode() + " Twitter configuration: " + ex.getMessage());
        }
        WebServiceSettings webServiceSettings = contest.getWebServiceSettings();
        TwitterStreamDTO twitterStreamDTO = new TwitterStreamDTO(twitterStream, contest.getHashtag(),
                webServiceSettings.getTwitterConsumerKey(), webServiceSettings.getTwitterConsumerSecret(),
                webServiceSettings.getTwitterAccessToken(), webServiceSettings.getTwitterAccessTokenSecret());
        streamMapping.put(contest.getId(), twitterStreamDTO);
    }

    /**
     * Terminates the twitter streaming for a contest
     *
     * @param contestId contest ID
     */
    public void stopTwitterStreaming(Long contestId) {
        TwitterStreamDTO twitterStreamDTO = streamMapping.get(contestId);
        if (twitterStreamDTO != null) {
            TwitterStream twitterStream = twitterStreamDTO.getTwitterStream();
            if (twitterStream != null) {
                twitterStream.shutdown();
                streamMapping.remove(contestId);
            }
        }
    }

    /**
     * Checks the streams and updates them if necessary
     */
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional(readOnly = true)
    public void detectTwitterConfigurationChanges() {
        for (Map.Entry<Long, TwitterStreamDTO> entry : streamMapping.entrySet()) {
            Contest contest = contestRepository.findOne(entry.getKey());
            // TODO stop stream if the contest is over
            if (ContestService.isContestOver(contest)) {
                stopTwitterStreaming(contest.getId());
            } else {
                TwitterStreamDTO twitterStreamDTO = entry.getValue();
                if (twitterStreamDTO != null &&
                        twitterStreamDTO.hasConfigChanged(contest.getHashtag(), contest.getWebServiceSettings())) {
                    stopTwitterStreaming(contest.getId());
                    startTwitterStreaming(contest);
                }
            }
        }
    }

    /**
     * Queries Twitter search API and gets tweets satisfying the search criteria
     *
     * @param hashtags hashtags divided by comma
     * @param count max number of results
     * @param pages start on this result page
     * @param sinceId find tweets newer than this tweet ID
     * @param maxId find tweets older than this tweet ID
     * @param contest contest
     * @return created tweet {@link Notification}
     * @throws TwitterException Twitter search failed and Twitter did not responded correctly
     */
    @Transactional
    public List<Notification> processAdditionalTweets(String hashtags, Integer count, Integer pages,
                                                      Long sinceId, Long maxId, final Contest contest) throws TwitterException {
        ConfigurationBuilder cb = createTwitterConfiguration(contest.getWebServiceSettings());

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        List<Notification> createdTweets = new ArrayList<>();

        Query query = new Query(hashtags);
        if (count != null) {
            query.setCount(count);
        }
        if (sinceId != null) {
            query.setSinceId(sinceId);
        }
        if (maxId != null) {
            query.setMaxId(maxId);
        }
        if (pages == null) {
            pages = 0;
        }
        QueryResult result = twitter.search(query);
        for (Status status : result.getTweets()) {
            Notification notification = onStatus(status, contest);
            createdTweets.add(notification);
        }
        for (int i = 0; i < pages - 1; i++) {
            if (result.hasNext()) {
                QueryResult result2 = twitter.search(result.nextQuery());
                for (Status status : result2.getTweets()) {
                    Notification notification = onStatus(status, contest);
                    createdTweets.add(notification);
                }
            }
        }

        return createdTweets;
    }

    /**
     * Saves the tweet and publishes it
     * <p/>
     * It checks, if the tweet is not already in database or if the author is not blacklisted.
     * If so, it skips the tweet
     *
     * @param notification  tweet notification
     * @param twitterStatus twitter status
     * @return created tweet
     */
    @Transactional
    private Notification saveTwitterNotification(Notification notification, Status twitterStatus) {
        if (notificationRepository.countByContestAndExternalIdAndNotificationType(notification.getContest(), notification.getExternalId(), NotificationType.TWITTER) > 0) {
            logger.info("Skip tweet " + notification.getExternalId() + " because of duplication.");
            return notification;
        }

        BlacklistedUser blacklistedUser = blacklistedUserRepository.findByUsernameAndBlacklistedUserType(twitterStatus.getUser().getScreenName(), BlacklistedUser.BlacklistedUserType.TWITTER);
        // skip, if user is in the blacklist
        if (blacklistedUser != null) {
            return notification;
        }
        if (twitterStatus.getRetweetedStatus() != null) {
            // skip, if author of retweeted tweet is in the blacklist
            blacklistedUser = blacklistedUserRepository.findByUsernameAndBlacklistedUserType(twitterStatus.getRetweetedStatus().getUser().getScreenName(), BlacklistedUser.BlacklistedUserType.TWITTER);
            if (blacklistedUser != null) {
                return notification;
            }
        }
        Notification persistedNotification = notificationRepository.save(notification);
        publishService.broadcastNotification(notification, notification.getContest());
        return persistedNotification;
    }

    @Transactional
    private Notification onStatus(final Status twitterStatus, final Contest contest) {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setTitle(twitterStatus.getUser().getScreenName());
        builder.setBody(parseTweetText(twitterStatus));
        builder.setHashtag(getHashtagsFromTweet(twitterStatus.getText()));
        builder.setNotificationType(NotificationType.TWITTER);
        builder.setExternalId(String.valueOf(twitterStatus.getId()));
        if (twitterStatus.getRetweetedStatus() != null) {
            builder.setParentId(twitterStatus.getRetweetedStatus().getId());
        }
        builder.setAuthorName(twitterStatus.getUser().getName());
        builder.setAuthorUsername(twitterStatus.getUser().getScreenName());
        builder.setProfilePictureUrl(twitterStatus.getUser().getProfileImageURL());
        builder.setTimestamp(twitterStatus.getCreatedAt());
        builder.setUrl(String.format(LINK_TO_TWEET, twitterStatus.getId()));
        builder.setContest(contest);

        if (!twitterStatus.isRetweet()) {
            String videoUrl = extractVideoUrlFromTweet(twitterStatus);
            if (StringUtils.isEmpty(videoUrl)) {
                // tweet does not contain video, extract image if it contains
                if (ArrayUtils.isNotEmpty(twitterStatus.getMediaEntities())) {
                    String imageUrl = twitterStatus.getMediaEntities()[0].getMediaURL();
                    builder.setThumbnailUrl(imageUrl + ":small");
                    builder.setImageUrl(imageUrl);
                }
            } else {
                String thumbnailUrl = twitterStatus.getMediaEntities()[0].getMediaURL();
                builder.setThumbnailUrl(thumbnailUrl + ":small");
                builder.setVideoUrl(videoUrl);
            }
        }

        // send the received notification
        return saveTwitterNotification(builder.build(), twitterStatus);
    }

    private static ConfigurationBuilder createTwitterConfiguration(final WebServiceSettings webServiceSettings) {
        if (StringUtils.isAnyEmpty(webServiceSettings.getTwitterConsumerKey(),
                webServiceSettings.getTwitterConsumerSecret(),
                webServiceSettings.getTwitterAccessToken(),
                webServiceSettings.getTwitterAccessTokenSecret())) {
            throw new IllegalArgumentException("Missing twitter configuration");
        }
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(webServiceSettings.getTwitterConsumerKey())
                .setOAuthConsumerSecret(webServiceSettings.getTwitterConsumerSecret())
                .setOAuthAccessToken(webServiceSettings.getTwitterAccessToken())
                .setOAuthAccessTokenSecret(webServiceSettings.getTwitterAccessTokenSecret());
        return cb;
    }

    /**
     * Finds Twitter hashtags, usernames, and URLs in the tweet
     *
     * @param status Twitter tweet
     * @return tweet message enhanced by HTML tags
     */
    private static String parseTweetText(final Status status) {
        String text;
        if (status.isRetweet() && status.getRetweetedStatus() != null) {
            text = "RT @" + status.getRetweetedStatus().getUser().getScreenName() + ": " + status.getRetweetedStatus().getText();
        } else {
            text = status.getText();
        }
        text = text.replaceAll("((https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])", "<a href='$1'>$1</a>");
        text = text.replaceAll("@([\\p{L}+0-9-_]+)", "<a href='http://twitter.com/$1'>@$1</a>");
        text = text.replaceAll("#([\\p{L}+0-9-_]+)", "<a href='https://twitter.com/search/?src=hash&amp;q=%23$1'>#$1</a>");
        return text;
    }

    /**
     * Gets all hashtags from the tweet body separated by |
     *
     * @param tweet tweet body
     * @return hashtags separated by |
     */
    private static String getHashtagsFromTweet(final String tweet) {
        Pattern pattern = Pattern.compile("(#[\\p{L}+0-9-_]+)");
        Matcher matcher = pattern.matcher(tweet);
        StringBuilder hashtags = new StringBuilder("|");
        while (matcher.find()) {
            hashtags.append(matcher.group().substring(1)).append("|");
        }
        return hashtags.toString();
    }

    /**
     * It parses out the video URL, which has {@link #VIDEO_FORMAT} format
     *
     * @param twitterStatus twitter status
     * @return video URL
     */
    private static String extractVideoUrlFromTweet(Status twitterStatus) {
        if (ArrayUtils.isNotEmpty(twitterStatus.getExtendedMediaEntities())) {
            for (ExtendedMediaEntity mediaEntity : twitterStatus.getExtendedMediaEntities()) {
                if ("video".equalsIgnoreCase(mediaEntity.getType())) {
                    for (ExtendedMediaEntity.Variant variant : mediaEntity.getVideoVariants()) {
                        if (VIDEO_FORMAT.equalsIgnoreCase(variant.getContentType())) {
                            return variant.getUrl();
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }

    private class TwitterStatusListener extends StatusAdapter {
        private Contest contest;

        public TwitterStatusListener(Contest contest) {
            this.contest = contest;
        }

        @Override
        public void onException(final Exception ex) {
            logger.error("Twitter stream error. Followed hashtag: " + contest.getHashtag(), ex);
        }

        @Override
        @Transactional
        public void onStatus(Status twitterStatus) {
            TwitterService.this.onStatus(twitterStatus, contest);
        }
    }
}
