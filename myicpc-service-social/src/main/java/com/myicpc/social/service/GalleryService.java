package com.myicpc.social.service;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.utils.lists.NotificationList;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service responsible for crowd gallery
 *
 * @author Roman Smetana
 */
@Service
@Transactional
public class GalleryService {
    public static final int POSTS_PER_PAGE = 30;
    private static final Pageable DEFAULT_PAGEABLE = new PageRequest(0, POSTS_PER_PAGE);

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * List of notification types present in the gallery
     */
    public static final List<NotificationType> GALLERY_TYPES = NotificationList.newList()
            .addTwitter()
            .addInstagram()
            .addVine()
            .addPicasa();

    /**
     * Gets photos and videos together
     *
     * @param contest contest
     * @return photos and videos notifications
     */
    public List<Notification> getGalleryNotifications(final Contest contest) {
        Page<Notification> timelineNotifications = notificationRepository.findByGalleryNotificationTypesOrderByIdDesc(GALLERY_TYPES, contest, DEFAULT_PAGEABLE);
        return timelineNotifications.getContent();
    }

    /**
     * Gets photos only
     *
     * @param contest contest
     * @return photos notifications
     */
    public List<Notification> getPhotosNotifications(final Contest contest) {
        Page<Notification> timelineNotifications = notificationRepository.findByPhotosNotificationTypesOrderByIdDesc(GALLERY_TYPES, contest, DEFAULT_PAGEABLE);
        return timelineNotifications.getContent();
    }

    /**
     * Gets videos only
     *
     * @param contest contest
     * @return videos notifications
     */
    public List<Notification> getVideosNotifications(final Contest contest) {
        Page<Notification> timelineNotifications = notificationRepository.findByVideosNotificationTypesOrderByIdDesc(GALLERY_TYPES, contest, DEFAULT_PAGEABLE);
        return timelineNotifications.getContent();
    }

    /**
     * Gets photos and videos older than notification with ID {@code lastNotificationId}
     *
     * @param contest            contest
     * @param lastNotificationId notification ID, which determines the deadline
     * @return photos and videos older than notification with ID {@code lastNotificationId}
     */
    public List<Notification> getGalleryNotifications(final Contest contest, Long lastNotificationId) {
        Page<Notification> timelineNotifications = notificationRepository.findByGalleryNotificationTypesFromNotificationIdOrderByIdDesc(lastNotificationId, GALLERY_TYPES, contest, DEFAULT_PAGEABLE);
        return timelineNotifications.getContent();
    }

    /**
     * Gets photos older than notification with ID {@code lastNotificationId}
     *
     * @param contest            contest
     * @param lastNotificationId notification ID, which determines the deadline
     * @return photos older than notification with ID {@code lastNotificationId}
     */
    public List<Notification> getPhotosNotifications(final Contest contest, Long lastNotificationId) {
        Page<Notification> timelineNotifications = notificationRepository.findByPhotosNotificationTypesFromNotificationIdOrderByIdDesc(lastNotificationId, GALLERY_TYPES, contest, DEFAULT_PAGEABLE);
        return timelineNotifications.getContent();
    }

    /**
     * Gets videos older than notification with ID {@code lastNotificationId}
     *
     * @param contest            contest
     * @param lastNotificationId notification ID, which determines the deadline
     * @return videos older than notification with ID {@code lastNotificationId}
     */
    public List<Notification> getVideosNotifications(final Contest contest, Long lastNotificationId) {
        Page<Notification> timelineNotifications = notificationRepository.findByVideosNotificationTypesFromNotificationIdOrderByIdDesc(lastNotificationId, GALLERY_TYPES, contest, DEFAULT_PAGEABLE);
        return timelineNotifications.getContent();
    }

    /**
     * Gets the lowest notification ID in {@code notifications}
     *
     * @param notifications sorted notifications by ID descending
     * @return the lowest notification ID
     */
    public Long getLastNotificationId(List<Notification> notifications) {
        Long lastNotificationId = -1L;
        if (!CollectionUtils.isEmpty(notifications) && notifications.size() == POSTS_PER_PAGE) {
            lastNotificationId = notifications.get(notifications.size() - 1).getId();
        }
        return lastNotificationId;
    }
}
