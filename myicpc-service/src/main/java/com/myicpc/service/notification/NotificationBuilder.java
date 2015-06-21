package com.myicpc.service.notification;

import com.google.common.base.Joiner;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.EntityObject;
import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;

import java.util.Date;
import java.util.List;

public class NotificationBuilder {
    private final Notification notification;

    public NotificationBuilder() {
        notification = new Notification();
        notification.setTimestamp(new Date());
    }

    public NotificationBuilder(final EntityObject entityObject) {
        this();
        notification.setEntityId(entityObject.getId());
    }

    public NotificationBuilder(final IdGeneratedContestObject contestObject) {
        this((EntityObject) contestObject);
        notification.setContest(contestObject.getContest());
    }

    public NotificationBuilder(final IdGeneratedContestObject contestObject, final NotificationType notificationType,
                               final NotificationRepository notificationRepository) {
        List<Notification> notifications = notificationRepository.findByContestAndEntityIdAndNotificationType(contestObject.getContest(),
                contestObject.getId(), notificationType);
        if (notifications.size() == 1) {
            notification = notifications.get(0);
        } else {
            notification = new Notification();
            notification.setTimestamp(new Date());
            notification.setEntityId(contestObject.getId());
            notification.setContest(contestObject.getContest());
        }
    }

    public Notification build() {
        return notification;
    }

    public void setTitle(final String title) {
        notification.setTitle(title);
    }

    public void setBody(final String body) {
        notification.setBody(body);
    }

    public void setNotificationType(final NotificationType notificationType) {
        notification.setNotificationType(notificationType);
    }

    public void setUrl(final String url) {
        notification.setUrl(url);
    }

    public void setImageUrl(String imageUrl) {
        notification.setImageUrl(imageUrl);
    }

    public void setVideoUrl(String videoUrl) {
        notification.setVideoUrl(videoUrl);
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        notification.setThumbnailUrl(thumbnailUrl);
    }

    public void setTimestamp(final Date timestamp) {
        notification.setTimestamp(timestamp);
    }

    public void setCode(final String code) {
        notification.setCode(code);
    }

    public void setAuthorName(String authorName) {
        notification.setAuthorName(authorName);
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        notification.setProfilePictureUrl(profilePictureUrl);
    }

    public void setContest(final Contest contest) {
        notification.setContest(contest);
    }

    public void setTeamId(Long teamId) {
        notification.setTeamId(teamId);
    }

    public void setHashtag(String hashtags) {
        notification.setHashtags(hashtags);
    }

    public void setHashtags(String... hashtags) {
        String hashtag = "|" + Joiner.on("|").join(hashtags) + "|";
        notification.setHashtags(hashtag);
    }

    public void setExternalId(String externalId) {
        notification.setExternalId(externalId);
    }

    public void setParentId(Long parentId) {
        notification.setParentId(parentId);
    }

    public void setOffensive() {
		notification.setOffensive(BadWordsFilter.isNotificationOffensive(notification));
    }
}
