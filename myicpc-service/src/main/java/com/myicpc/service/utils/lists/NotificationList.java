package com.myicpc.service.utils.lists;

import com.myicpc.model.social.Notification.NotificationType;

import java.util.ArrayList;

/**
 * List of {@link NotificationType} which allows fluent interface
 *
 * @author Roman Smetana
 */
public class NotificationList extends ArrayList<NotificationType> {
    private static final long serialVersionUID = 7109072774414505810L;

    public static NotificationList newList() {
        return new NotificationList();
    }

    public NotificationList addScoreboardSuccess() {
        this.add(NotificationType.SCOREBOARD_SUCCESS);
        return this;
    }

    public NotificationList addScoreboardFailed() {
        this.add(NotificationType.SCOREBOARD_FAILED);
        return this;
    }

    public NotificationList addScoreboardSubmitted() {
        this.add(NotificationType.SCOREBOARD_SUBMIT);
        return this;
    }

    public NotificationList addTwitter() {
        this.add(NotificationType.TWITTER);
        return this;
    }

    public NotificationList addArticle() {
        this.add(NotificationType.ARTICLE);
        return this;
    }

    public NotificationList addAdminNotification() {
        this.add(NotificationType.ADMIN_NOTIFICATION);
        return this;
    }

    public NotificationList addGallery() {
        this.add(NotificationType.GALLERY);
        return this;
    }

    public NotificationList addVine() {
        this.add(NotificationType.VINE);
        return this;
    }

    public NotificationList addInstagramImage() {
        this.add(NotificationType.INSTAGRAM_IMAGE);
        return this;
    }

    public NotificationList addInstagramVideo() {
        this.add(NotificationType.INSTAGRAM_VIDEO);
        return this;
    }

    public NotificationList addPollOpen() {
        this.add(NotificationType.POLL_OPEN);
        return this;
    }

    public NotificationList addPollClose() {
        this.add(NotificationType.POLL_CLOSE);
        return this;
    }

    public NotificationList addYoutube() {
        this.add(NotificationType.YOUTUBE_VIDEO);
        return this;
    }

    public NotificationList addScheduleEventOpen() {
        this.add(NotificationType.SCHEDULE_EVENT_OPEN);
        return this;
    }

    public NotificationList addQuestWinnerTwitter() {
        this.add(NotificationType.QUEST_VOTE_WINNER_TWITTER);
        return this;
    }

    public NotificationList addQuestWinnerVine() {
        this.add(NotificationType.QUEST_VOTE_WINNER_VINE);
        return this;
    }

    public NotificationList addQuestChallenge() {
        this.add(NotificationType.QUEST_CHALLENGE);
        return this;
    }

    public NotificationList addAnalystMessage() {
        this.add(NotificationType.ANALYST_MESSAGE);
        return this;
    }
}
