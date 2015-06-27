package com.myicpc.service.dto;

import com.myicpc.model.social.Notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Smetana
 */
public class TimelineFeaturedNotificationsDTO implements Serializable {
    private static final long serialVersionUID = 6459734169744705003L;

    private List<Notification> adminNotifications;
    private List<Notification> questNotifications;
    private List<Notification> pollNotifications;

    public TimelineFeaturedNotificationsDTO() {
        adminNotifications = new ArrayList<>();
        questNotifications = new ArrayList<>();
        pollNotifications = new ArrayList<>();
    }

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
