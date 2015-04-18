package com.myicpc.service.quest;

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

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
public class QuestService {
    public static final int POSTS_PER_PAGE = 30;

    /**
     * Notification types displayed on the quest timeline
     */
    public static final List<NotificationType> QUEST_TIMELINE_TYPES = NotificationList.newList()
            .addTwitter()
            .addInstagram()
            .addVine();

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getQuestNotifications(final Contest contest) {
        Pageable pageable = new PageRequest(0, POSTS_PER_PAGE);
        Page<Notification> questNotifications = notificationRepository.findByHashtagAndNotificationTypes(
                String.format("%%|%s%%", contest.getQuestConfiguration().getHashtagPrefix()),
                QUEST_TIMELINE_TYPES,
                contest,
                pageable);
        return questNotifications.getContent();
    }

}
