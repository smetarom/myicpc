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

import java.util.Date;
import java.util.List;

/**
 * Service responsible for timeline
 *
 * @author Roman Smetana
 */
@Service
public class TimelineService {
    public static final int POSTS_PER_PAGE = 30;
    /**
     * Notification types presented on the timeline
     */
    public static final List<NotificationType> TIMELINE_TYPES = NotificationList.newList()
            .addScoreboardSuccess()
            .addTeamAnalystMessage()
            .addAnalystMessage()
            .addTwitter()
            .addInstagram()
            .addVine()
            .addPicasa()
            .addOfifcialGallery()
            .addQuestChallenge()
            .addAdminNotification()
            .addScheduleEventOpen();

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Gets latest {@link #POSTS_PER_PAGE} notifications with type from {@link #TIMELINE_TYPES}
     *
     * @param contest contest
     * @return latest notifications
     */
    public List<Notification> getTimelineNotifications(Contest contest) {
        Pageable pageable = new PageRequest(0, POSTS_PER_PAGE);
        Page<Notification> timelineNotifications = notificationRepository.findByNotificationTypesOrderByIdDesc(TIMELINE_TYPES, contest, pageable);
        return timelineNotifications.getContent();
    }

    /**
     * Gets {@link #POSTS_PER_PAGE} notifications with type from {@link #TIMELINE_TYPES}
     * created after {@code lastTimestamp}
     *
     * @param lastTimestamp deadline timestamp, when notifications are created
     * @param contest contest
     * @return latest notifications before {@code lastTimestamp}
     */
    public List<Notification> getTimelineNotifications(Long lastTimestamp, Contest contest) {
        Pageable pageable = new PageRequest(0, POSTS_PER_PAGE);
        Date timestamp = new Date(lastTimestamp);
        Page<Notification> timelineNotifications = notificationRepository.findByNotificationTypesOrderByIdDesc(timestamp, TIMELINE_TYPES, contest, pageable);
        return timelineNotifications.getContent();
    }
}
