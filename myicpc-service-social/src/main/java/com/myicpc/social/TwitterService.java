package com.myicpc.social;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.BlacklistedUser;
import com.myicpc.model.social.Notification;
import org.apache.commons.collections4.CollectionUtils;
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

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
public class TwitterService extends ASocialService {
    private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);

    public void startTwitterStreaming(Contest contest) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(contest.getWebServiceSettings().getTwitterConsumerKey())
            .setOAuthConsumerSecret(contest.getWebServiceSettings().getTwitterConsumerSecret())
            .setOAuthAccessToken(contest.getWebServiceSettings().getTwitterAccessToken())
            .setOAuthAccessTokenSecret(contest.getWebServiceSettings().getTwitterAccessTokenSecret());
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterStream.addListener(new TwitterStatusListener(contest));
        twitterStream.filter(new FilterQuery(0, null, new String[] { "#" + contest.getHashtag() }));
    }

    @Transactional
    public void createTwitterNotificationFromStatus(final Status status, final Contest contest) {
        List<Notification> existingTweets = notificationRepository.findByContestAndExternalIdAndNotificationType(contest, String.valueOf(status.getId()), NotificationType.TWITTER);

        if (!CollectionUtils.isEmpty(existingTweets)) {
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

        Notification notification = notificationService.createNotification(status, contest);
        publishService.broadcastNotification(notification, contest);
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
