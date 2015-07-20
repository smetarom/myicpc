package com.myicpc.service.kiosk;

import com.google.gson.JsonArray;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.kiosk.KioskContent;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.kiosk.KioskContentRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.utils.lists.NotificationList;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service for public kiosk views
 *
 * @author Roman Smetana
 */
@Service
public class KioskService {
    public static final int POSTS_PER_PAGE = 30;

    /**
     * Notification types presented on the kiosk
     */
    public static final List<NotificationType> KIOSK_TYPES = NotificationList.newList()
            .addTeamAnalystMessage()
            .addAnalystMessage()
            .addTwitter()
            .addInstagram()
            .addVine()
            .addPicasa()
            .addQuestChallenge()
            .addYoutube()
            .addPollOpen()
            .addAdminNotification()
            .addScheduleEventOpen();

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private KioskContentRepository kioskContentRepository;

    /**
     * Finds the last {@code POSTS_PER_PAGE} notifications with {@code KIOSK_TYPES} types
     *
     * @param contest contest
     * @return notification list sorted from oldest to newest
     */
    @Transactional(readOnly = true)
    public List<Notification> getKioskNotifications(Contest contest) {
        Pageable pageable = new PageRequest(0, POSTS_PER_PAGE);
        Page<Notification> timelineNotifications = notificationRepository.findByNotificationTypesOrderByIdDesc(KIOSK_TYPES, contest, pageable);
        List<Notification> notifications = new ArrayList<>(timelineNotifications.getContent());
        Collections.reverse(notifications);
        return notifications;
    }

    /**
     * JSON representation of {@link #getKioskNotifications(Contest)}
     *
     * @param contest contest
     * @return JSON object with notifications
     */
    public JsonArray getKioskNotificationsJSON(Contest contest) {
        List<Notification> notifications = getKioskNotifications(contest);
        return NotificationService.getNotificationInJson(notifications);
    }

    /**
     * Finds the active custom kiosk page content
     *
     * @param contest contest
     * @return active {@link KioskContent} or null, if there is no active
     */
    @Transactional(readOnly = true)
    public KioskContent getActiveKioskContent(Contest contest) {
        List<KioskContent> activeList = kioskContentRepository.findByActive(true);
        if (CollectionUtils.isEmpty(activeList)) {
            return null;
        }
        return activeList.get(0);
    }
}
