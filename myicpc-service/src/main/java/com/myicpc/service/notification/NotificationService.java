package com.myicpc.service.notification;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.commons.utils.CookieUtils;
import com.myicpc.commons.utils.TimeUtils;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.AdminNotification;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.AdminNotificationRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.utils.lists.NotificationList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class provides services to manage {@link Notification}
 *
 * @author Roman Smetana
 */
@Service("notificationService")
@Transactional
public class NotificationService {
    public static final List<NotificationType> FEATURED_NOTIFICATION_TYPES = NotificationList.newList()
            .addAdminNotification()
            .addQuestChallenge()
            .addPollOpen();

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AdminNotificationRepository adminNotificationRepository;

    public void markDeleted(Notification notification) {
        notification.setDeleted(true);
        notificationRepository.save(notification);
    }

    public void markDeleted(List<Notification> notifications) {
        for (Notification notification : notifications) {
            notification.setDeleted(true);
        }
        notificationRepository.save(notifications);
    }

    public void updateAdminNotification(final AdminNotification adminNotification) {
        adminNotificationRepository.save(adminNotification);
        // TODO
//        List<Notification> notifications = notificationRepository.findByEntityIdAndNotificationType(adminNotification.getId(), NotificationType.ADMIN_NOTIFICATION);
//        for (Notification notification : notifications) {
//            notification.setTitle(WikiModel.toHtml(adminNotification.getTitle()));
//            notification.setBody(WikiModel.toHtml(adminNotification.getBody()));
//        }
//        notificationRepository.save(notifications);
//        if (adminNotification.isPublished()) {
//            PublishService.broadcastNotification(notificationForAdminNotification(adminNotification));
//        }
    }

    public void deleteAdminNotification(final AdminNotification adminNotification) {
        adminNotificationRepository.delete(adminNotification);

        // TODO
//        deleteNoficicationsForEntity(adminNotification.getId(), NotificationType.ADMIN_NOTIFICATION);
    }

    public List<Notification> getFeaturedQuestNotifications(final List<Long> ignoredFeatured) {
        List<Notification> notifications = new ArrayList<>();
        notifications.addAll(notificationRepository.findCurrentQuestChallengeNotifications(new Date(), ignoredFeatured));

        Collections.sort(notifications, new FeaturedNotificationComparator());

        return notifications;
    }

    /**
     * Return list of featured notification, which are presented to user as
     * important on the top of the page
     *
     * @param ignoredFeatured ids of notifications ignored by user
     * @param contest
     * @return list of featured notifications
     */
    public List<Notification> getFeaturedNotifications(final List<Long> ignoredFeatured, final Contest contest) {
        return notificationRepository.findFeaturedNotifications(new Date(), ignoredFeatured, contest);
    }

    public Long countFeaturedNotifications(final List<Long> ignoredFeatured, final Contest contest) {
        return notificationRepository.countFeaturedNotifications(new Date(), ignoredFeatured, contest);
    }

    public static JsonArray getNotificationInJson(final List<Notification> notifications) {
        JsonArray arr = new JsonArray();
        for (Notification notification : notifications) {
            arr.add(getNotificationInJson(notification));
        }
        return arr;
    }

    /**
     * Returns JSON representation of list of notifications
     *
     * @param notifications
     *            list of notifications
     * @return JSON representation of list of notifications
     */
    public static JsonArray getNotificationsInJson(final List<Notification> notifications) {
        JsonArray arr = new JsonArray();
        if (notifications != null) {
            for (Notification notification : notifications) {
                arr.add(getNotificationInJson(notification));
            }
        }
        return arr;
    }

    /**
     * Returns JSON representation of {@link Notification}
     *
     * @param notification
     * @return JSON representation
     */
    public static JsonObject getNotificationInJson(final Notification notification) {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.ENGLISH);
        JsonObject notificationObject = new JsonObject();
        notificationObject.addProperty("id", notification.getId());
        notificationObject.addProperty("externalId", notification.getExternalId());
        notificationObject.addProperty("type", notification.getNotificationType().getCode());
        notificationObject.addProperty("title", notification.getTitle());
        notificationObject.addProperty("body", notification.getBody());
        notificationObject.addProperty("authorName", notification.getAuthorName());
        notificationObject.addProperty("url", notification.getUrl());
        notificationObject.addProperty("imageUrl", notification.getImageUrl());
        notificationObject.addProperty("videoUrl", notification.getVideoUrl());
        notificationObject.addProperty("entityId", notification.getEntityId());
        notificationObject.addProperty("code", notification.getCode());
        Date timestamp = TimeUtils.convertUTCDateToLocal(notification.getTimestamp(), notification.getContest().getTimeDifference());
        notificationObject.addProperty("timestamp", timestamp != null ? formatter.format(timestamp) : "");
        notificationObject.addProperty("profileUrl", notification.getProfilePictureUrl());

        return notificationObject;
    }

    public static List<Long> getFeaturedIdsFromCookie(final HttpServletRequest request, final HttpServletResponse response) {
        String cookieName = "ignoreFeaturedNotifications";
        String ignoreFeaturedNotifications = CookieUtils.getCookie(request, cookieName);
        List<Long> ignoredFeatured = new ArrayList<>();
        ignoredFeatured.add(-1L);
        if (!StringUtils.isEmpty(ignoreFeaturedNotifications)) {
            try {
                String[] ss = ignoreFeaturedNotifications.split(",");
                for (String s : ss) {
                    ignoredFeatured.add(Long.parseLong(s));
                }
            } catch (Throwable ex) {
                CookieUtils.removeCookie(request, response, cookieName);
            }
        }
        return ignoredFeatured;
    }

    private static class FeaturedNotificationComparator implements Comparator<Notification> {
        @Override
        public int compare(final Notification n1, final Notification n2) {
            if (n1.getTimestamp() == null) {
                return -1;
            }
            if (n2.getTimestamp() == null) {
                return 1;
            }
            return -1 * n1.getTimestamp().compareTo(n2.getTimestamp());
        }
    }
}
