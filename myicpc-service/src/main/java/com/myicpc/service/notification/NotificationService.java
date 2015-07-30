package com.myicpc.service.notification;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.commons.utils.CookieUtils;
import com.myicpc.commons.utils.TimeUtils;
import com.myicpc.commons.utils.WikiUtils;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.AdminNotification;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.AdminNotificationRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.dto.TimelineFeaturedNotificationsDTO;
import com.myicpc.service.publish.PublishService;
import com.myicpc.service.utils.lists.NotificationList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class provides services to manage {@link Notification}
 *
 * @author Roman Smetana
 */
@Service("notificationService")
public class NotificationService {
    public static final List<NotificationType> FEATURED_NOTIFICATION_TYPES = NotificationList.newList()
            .addAdminNotification()
            .addQuestChallenge()
            .addPollOpen();

    /**
     * Modifies a given {@link Notification}
     */
    public interface NotificationModifier {
        void modify(Notification notification);
    }

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AdminNotificationRepository adminNotificationRepository;

    @Autowired
    private PublishService publishService;

    @Transactional
    public void markDeleted(Notification notification) {
        notification.setDeleted(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markDeleted(List<Notification> notifications) {
        for (Notification notification : notifications) {
            notification.setDeleted(true);
        }
        notificationRepository.save(notifications);
    }

    @Transactional
    public void createNotificationsForNewAdminNotifications(Contest contest) {
        List<AdminNotification> adminNotifications = adminNotificationRepository.findAllNonpublishedStartedNotifications(new Date(), contest);
        for (AdminNotification adminNotification : adminNotifications) {
            adminNotification.setPublished(true);

            NotificationBuilder builder = new NotificationBuilder(adminNotification);
            builder.setTitle(adminNotification.getTitle());
            builder.setBody(WikiUtils.parseWikiSyntax(adminNotification.getBody()));
            builder.setEntityId(adminNotification.getId());
            builder.setNotificationType(NotificationType.ADMIN_NOTIFICATION);
            builder.setImageUrl(adminNotification.getImageUrl());
            builder.setVideoUrl(adminNotification.getVideoUrl());
            builder.setContest(contest);
            Notification notification = notificationRepository.save(builder.build());
            publishService.broadcastNotification(notification, contest);
        }
    }

    @Transactional
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

    @Transactional
    public void deleteAdminNotification(final AdminNotification adminNotification) {
        adminNotificationRepository.delete(adminNotification);

        // TODO
//        deleteNoficicationsForEntity(adminNotification.getId(), NotificationType.ADMIN_NOTIFICATION);
    }

    public void modifyExistingNotifications(final IdGeneratedContestObject entity, final NotificationType notificationType, final NotificationModifier modifier) {
        List<Notification> notifications = notificationRepository.findByContestAndEntityIdAndNotificationType(entity.getContest(), entity.getId(), notificationType);
        for (Notification notification : notifications) {
            modifier.modify(notification);
        }
        notificationRepository.save(notifications);
    }

    public void deleteExistingNotifications(final IdGeneratedContestObject entity, final NotificationType notificationType) {
        notificationRepository.deleteByEntityIdAndNotificationType(entity.getId(), notificationType, entity.getContest());
    }

    public TimelineFeaturedNotificationsDTO getTimelineFeaturedNotifications(final List<Long> ignoredFeatured, final Contest contest) {
        TimelineFeaturedNotificationsDTO timelineFeaturedNotifications = new TimelineFeaturedNotificationsDTO();
        List<Notification> featuredNotifications = getFeaturedNotifications(ignoredFeatured, contest);
        for (Notification notification : featuredNotifications) {
            timelineFeaturedNotifications.addNotification(notification);
        }

        return timelineFeaturedNotifications;
    }

    /**
     * Return list of featured notification, which are presented to user as
     * important on the top of the page
     *
     * @param ignoredFeatured ids of notifications ignored by user
     * @param contest
     * @return list of featured notifications
     */
    @Transactional(readOnly = true)
    public List<Notification> getFeaturedNotifications(final List<Long> ignoredFeatured, final Contest contest) {
        return getFeaturedNotifications(ignoredFeatured, contest, null);
    }

    @Transactional(readOnly = true)
    public List<Notification> getFeaturedNotifications(final List<Long> ignoredFeatured, final Contest contest, final Pageable pageable) {
        return notificationRepository.findFeaturedNotifications(new Date(), ignoredFeatured, contest, pageable);
    }

    @Transactional(readOnly = true)
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
        notificationObject.addProperty("thumbnailUrl", notification.getThumbnailUrl());
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
}
