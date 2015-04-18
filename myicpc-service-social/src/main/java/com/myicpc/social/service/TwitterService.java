package com.myicpc.social.service;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.BlacklistedUser;
import com.myicpc.model.social.Notification;
import com.myicpc.service.notification.NotificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class TwitterService extends ASocialService {
    private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);

    public void processReceivedNotification(final Notification receivedNotification) {
        Contest contest = contestRepository.getOne(receivedNotification.getContest().getId());
        Notification existingTweets = notificationRepository.findByContestAndExternalIdAndNotificationType(contest, String.valueOf(receivedNotification.getExternalId()), NotificationType.TWITTER);

        if (existingTweets != null) {
            logger.info("Skip tweet " + receivedNotification.getExternalId() + " because of duplication.");
            return;
        }

        BlacklistedUser blacklistedUser = blacklistedUserRepository.findByUsernameAndBlacklistedUserType(receivedNotification.getAuthorUsername(), BlacklistedUser.BlacklistedUserType.TWITTER);
        // skip, if user is in the blacklist
        if (blacklistedUser != null) {
            return;
        }
        // TODO resolve what to do with retweets
//        if (receivedNotification.getRetweetedId() != null) {
//            // skip, if author of retweeted tweet is in the blacklist
//            blacklistedUser = blacklistedUserRepository.findByUsernameAndBlacklistedUserType(status.getRetweetedStatus().getUser().getScreenName(), BlacklistedUser.BlacklistedUserType.TWITTER);
//            if (blacklistedUser != null) {
//                return;
//            }
//        }

        receivedNotification.setContest(contest);
        notificationRepository.save(receivedNotification);
        publishService.broadcastNotification(receivedNotification, contest);
    }

    public void startTwitterStreaming(Contest contest) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(contest.getWebServiceSettings().getTwitterConsumerKey())
                .setOAuthConsumerSecret(contest.getWebServiceSettings().getTwitterConsumerSecret())
                .setOAuthAccessToken(contest.getWebServiceSettings().getTwitterAccessToken())
                .setOAuthAccessTokenSecret(contest.getWebServiceSettings().getTwitterAccessTokenSecret());
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterStream.addListener(new TwitterStatusListener(contest));
        twitterStream.filter(new FilterQuery(0, null, new String[]{"#" + contest.getHashtag()}));
    }

    @Transactional
    public void createTwitterNotificationFromStatus(final Status status, final Contest contest) {
        Notification existingTweets = notificationRepository.findByContestAndExternalIdAndNotificationType(contest, String.valueOf(status.getId()), NotificationType.TWITTER);

        if (existingTweets != null) {
            logger.info("Skip tweet " + status.getId() + " because of duplication.");
            return;
        }

        BlacklistedUser blacklistedUser = blacklistedUserRepository.findByUsernameAndBlacklistedUserType(status.getUser().getScreenName(), BlacklistedUser.BlacklistedUserType.TWITTER);
        // skip, if user is in the blacklist
        if (blacklistedUser != null) {
            return;
        }
        if (status.getRetweetedStatus() != null) {
            // skip, if author of retweeted tweet is in the blacklist
            blacklistedUser = blacklistedUserRepository.findByUsernameAndBlacklistedUserType(status.getRetweetedStatus().getUser().getScreenName(), BlacklistedUser.BlacklistedUserType.TWITTER);
            if (blacklistedUser != null) {
                return;
            }
        }

        Notification notification = createNotification(status, contest);
        publishService.broadcastNotification(notification, contest);
    }

    @Transactional
    public Notification createNotification(final Status twitterStatus, final Contest contest) {
        NotificationBuilder builder = new NotificationBuilder();
        builder.setTitle(twitterStatus.getUser().getScreenName());
        builder.setBody(parseTweetText(twitterStatus));
        builder.setHashtag(getHashtagsFromTweet(twitterStatus.getText()));
        builder.setNotificationType(NotificationType.TWITTER);
        builder.setExternalId(String.valueOf(twitterStatus.getId()));
        if (twitterStatus.getRetweetedStatus() != null) {
            builder.setRetweetedId(twitterStatus.getRetweetedStatus().getId());
        }
        builder.setAuthorName(twitterStatus.getUser().getName());
        builder.setProfilePictureUrl(twitterStatus.getUser().getProfileImageURL());
        builder.setTimestamp(twitterStatus.getCreatedAt());
        builder.setContest(contest);

        if (!twitterStatus.isRetweet()) {
            if (twitterStatus.getMediaEntities() != null && twitterStatus.getMediaEntities().length > 0) {
                String imageUrl = twitterStatus.getMediaEntities()[0].getMediaURL();
                builder.setThumbnailUrl(imageUrl + ":small");
                builder.setImageUrl(imageUrl);
            }
        }

        return notificationRepository.save(builder.build());
    }

    /**
     * Finds Twitter hashtags, usernames, and URLs in the tweet
     *
     * @param status Twitter tweet
     * @return tweet message enhanced by HTML tags
     */
    protected static String parseTweetText(final Status status) {
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
     * @param tweet tweet body
     * @return hashtags separated by |
     */
    protected static String getHashtagsFromTweet(final String tweet) {
        Pattern pattern = Pattern.compile("(#[\\p{L}+0-9-_]+)");
        Matcher matcher = pattern.matcher(tweet);
        StringBuilder hashtags = new StringBuilder("|");
        while (matcher.find()) {
            hashtags.append(matcher.group().substring(1)).append("|");
        }
        return hashtags.toString();
    }

    class TwitterStatusListener implements StatusListener {
        private Contest contest;

        public TwitterStatusListener(Contest contest) {
            this.contest = contest;
        }

        @Override
        public void onException(final Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        @Override
        public void onDeletionNotice(final StatusDeletionNotice statusDeletionNotice) {
            // TODO it is never called
//            webService.deleteTweetByTweetId(statusDeletionNotice.getStatusId());
        }

        @Override
        public void onScrubGeo(final long userId, final long upToStatusId) {
        }

        @Override
        public void onStallWarning(StallWarning stallWarning) {
        }

        @Override
        public void onStatus(Status status) {
            TwitterService.this.createTwitterNotificationFromStatus(status, contest);
        }

        @Override
        public void onTrackLimitationNotice(int code) {
        }
    }
}
