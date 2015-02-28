package com.myicpc.service.timeline;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.utils.lists.NotificationList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class TimelineService {
    public static final int POSTS_PER_PAGE = 30;
    /**
     * Notification types presented on the timeline
     */
    public static final List<NotificationType> TIMELINE_TYPES = NotificationList.newList()
            .addScoreboardSuccess()
            .addAnalystMessage()
            .addTwitter()
            .addInstagram()
            .addVine();

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getTimelineNotifications(Contest contest) {
        Pageable pageable = new PageRequest(0, POSTS_PER_PAGE);
        Page<Notification> timelineNotifications = notificationRepository.findByNotificationTypesOrderByIdDesc(TIMELINE_TYPES, contest, pageable);
        return timelineNotifications.getContent();
    }


}