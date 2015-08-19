package com.myicpc.social.service;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.contest.WebServiceSettings;
import com.myicpc.model.social.BlacklistedUser;
import com.myicpc.model.social.Notification;
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
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roman Smetana
 */
@Service
public class TwitterService extends ASocialService {
    private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);
    private static final String VIDEO_FORMAT = "video/mp4";
    private static final String LINK_TO_TWEET = "https://twitter.com/statuses/%s";

    private static final ConcurrentMap<Long, TwitterStreamDTO> streamMapping = new ConcurrentHashMap<>();

    public void startTwitterStreaming(Long contestId) {
        Contest contest = contestRepository.findOne(contestId);
        startTwitterStreaming(contest);
    }

    public void startTwitterStreaming(Contest contest) {
        TwitterStream twitterStream = null;
        ConfigurationBuilder cb = createTwitterConfiguration(contest);
        twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterStream.addListener(new TwitterStatusListener(contest));
        twitterStream.filter(new FilterQuery(0, null, new String[]{"#" + contest.getHashtag()}));

        WebServiceSettings webServiceSettings = contest.getWebServiceSettings();
        TwitterStreamDTO twitterStreamDTO = new TwitterStreamDTO(twitterStream, contest.getHashtag(),
                webServiceSettings.getTwitterConsumerKey(), webServiceSettings.getTwitterConsumerSecret(),
                webServiceSettings.getTwitterAccessToken(), webServiceSettings.getTwitterAccessTokenSecret());
        streamMapping.put(contest.getId(), twitterStreamDTO);
    }

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

    @Scheduled(cron = "0 */1 * * * *")
    @Transactional(readOnly = true)
    public void detectTwitterConfigurationChanges() {
        for (Map.Entry<Long, TwitterStreamDTO> entry : streamMapping.entrySet()) {
            Contest contest = contestRepository.findOne(entry.getKey());
            // TODO stop stream if the contest is over
            TwitterStreamDTO twitterStreamDTO = entry.getValue();
            if (twitterStreamDTO.hasConfigChanged(contest.getHashtag(), contest.getWebServiceSettings())) {
                stopTwitterStreaming(contest.getId());
                startTwitterStreaming(contest);
            }
        }
    }

    @Transactional
    private void saveTwitterNotification(Notification notification, Status twitterStatus) {
        if (notificationRepository.countByContestAndExternalIdAndNotificationType(notification.getContest(), notification.getExternalId(), NotificationType.TWITTER) > 0) {
            logger.info("Skip tweet " + notification.getExternalId() + " because of duplication.");
            return;
        }

        BlacklistedUser blacklistedUser = blacklistedUserRepository.findByUsernameAndBlacklistedUserType(twitterStatus.getUser().getScreenName(), BlacklistedUser.BlacklistedUserType.TWITTER);
        // skip, if user is in the blacklist
        if (blacklistedUser != null) {
            return;
        }
        if (twitterStatus.getRetweetedStatus() != null) {
            // skip, if author of retweeted tweet is in the blacklist
            blacklistedUser = blacklistedUserRepository.findByUsernameAndBlacklistedUserType(twitterStatus.getRetweetedStatus().getUser().getScreenName(), BlacklistedUser.BlacklistedUserType.TWITTER);
            if (blacklistedUser != null) {
                return;
            }
        }
        notificationRepository.save(notification);
        publishService.broadcastNotification(notification, notification.getContest());
    }

    private ConfigurationBuilder createTwitterConfiguration(final Contest contest) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(contest.getWebServiceSettings().getTwitterConsumerKey())
                .setOAuthConsumerSecret(contest.getWebServiceSettings().getTwitterConsumerSecret())
                .setOAuthAccessToken(contest.getWebServiceSettings().getTwitterAccessToken())
                .setOAuthAccessTokenSecret(contest.getWebServiceSettings().getTwitterAccessTokenSecret());
        return cb;
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
        public void onStatus(Status twitterStatus) {
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
            TwitterService.this.saveTwitterNotification(builder.build(), twitterStatus);
        }

        /**
         * Finds Twitter hashtags, usernames, and URLs in the tweet
         *
         * @param status
         *            Twitter tweet
         * @return tweet message enhanced by HTML tags
         */
        private String parseTweetText(final Status status) {
            String text = null;
            if (status.isRetweet() && status.getRetweetedStatus() != null) {
                text = "RT @" + status.getRetweetedStatus().getUser().getScreenName() + ": " + status.getRetweetedStatus().getText();
            } else {
                text = status.getText();
            }
            text = text.replaceAll("((https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])", "<a href='$1'>$1</a>");
            text = text.replaceAll("@([\\p{L}+0-9-_]+)", "<a href='http://twitter.com/$1'>@$1</a>");
            text = text.replaceAll("#([\\p{L}+0-9-_]+)", "<a href='https://twitter.com/search/?src=hash&amp;q=%23$1'>#$1</a>");
            text = text.replaceAll("([\\ud800-\\udbff\\udc00-\\udfff])", "");
            return text;
        }

        /**
         * Gets all hashtags from the tweet body separated by |
         *
         * @param tweet
         *            tweet body
         * @return hashtags separated by |
         */
        private String getHashtagsFromTweet(final String tweet) {
            Pattern pattern = Pattern.compile("(#[\\p{L}+0-9-_]+)");
            Matcher matcher = pattern.matcher(tweet);
            StringBuilder hashtags = new StringBuilder("|");
            while (matcher.find()) {
                hashtags.append(matcher.group().substring(1)).append("|");
            }
            return hashtags.toString();
        }

        private String extractVideoUrlFromTweet(Status twitterStatus) {
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
    }
}
