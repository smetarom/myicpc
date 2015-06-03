package com.myicpc.service.kiosk;

import com.google.gson.JsonArray;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.utils.lists.NotificationList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
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

    public List<Notification> getKioskNotifications(Contest contest) {
        Pageable pageable = new PageRequest(0, POSTS_PER_PAGE);
        Page<Notification> timelineNotifications = notificationRepository.findByNotificationTypesOrderByIdDesc(KIOSK_TYPES, contest, pageable);
        List<Notification> notifications = new ArrayList<>(timelineNotifications.getContent());
        Collections.reverse(notifications);
        return notifications;
    }

    public JsonArray getKioskNotificationsJSON(Contest contest) {
        List<Notification> notifications = getKioskNotifications(contest);
        return NotificationService.getNotificationInJson(notifications);
    }
}
