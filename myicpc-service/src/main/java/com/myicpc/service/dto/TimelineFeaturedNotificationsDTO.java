package com.myicpc.service.dto;

import com.myicpc.model.social.Notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Holder for featured {@link Notification}, which splits them into categories
 *
 * @author Roman Smetana
 */
public class TimelineFeaturedNotificationsDTO implements Serializable {
    private static final long serialVersionUID = 6459734169744705003L;

    /**
     * Notifications created by MyICPC admin users
     */
    private List<Notification> adminNotifications;
    /**
     * Notifications from Quest
     */
    private List<Notification> questNotifications;
    /**
     * Notifications from polls
     */
    private List<Notification> pollNotifications;

    /**
     * Constructor
     */
    public TimelineFeaturedNotificationsDTO() {
        adminNotifications = new ArrayList<>();
        questNotifications = new ArrayList<>();
        pollNotifications = new ArrayList<>();
    }

    /**
     * Adds a {@code notification} to the correct category based on {@link Notification#notificationType}
     * <p>
     * If it does not belong to any category, the notification is ignored
     *
     * @param notification notification to be added
     */
    public void addNotification(final Notification notification) {
        if (notification != null && notification.getNotificationType() != null) {
            if (notification.getNotificationType().isAdminNotification()) {
                adminNotifications.add(notification);
            } else if (notification.getNotificationType().isQuestChallenge()) {
                questNotifications.add(notification);
            } else if (notification.getNotificationType().isPollOpen()) {
                pollNotifications.add(notification);
            }
        }
    }

    public List<Notification> getAdminNotifications() {
        return adminNotifications;
    }

    public List<Notification> getQuestNotifications() {
        return questNotifications;
    }

    public List<Notification> getPollNotifications() {
        return pollNotifications;
    }
}
