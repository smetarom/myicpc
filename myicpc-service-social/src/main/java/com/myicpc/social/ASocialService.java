package com.myicpc.social;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.BlacklistedUser;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.social.BlacklistedUserRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.publish.PublishService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Roman Smetana
 */
public abstract class ASocialService {
    @Autowired
    protected NotificationService notificationService;

    @Autowired
    protected BlacklistedUserRepository blacklistedUserRepository;

    @Autowired
    protected NotificationRepository notificationRepository;

    @Autowired
    protected PublishService publishService;

    /**
     * Persists search results from social networks
     *
     * @param notifications
     *            list of results
     * @param blacklist
     *            list of blacklisted usernames
     */
    protected void saveSearchList(List<Notification> notifications, BlacklistedUser.BlacklistedUserType blacklistedUserType, Contest contest) {
        Set<String> blacklist = new HashSet<>(blacklistedUserRepository.getUsernameByBlacklistedUserType(blacklistedUserType));

        for (int i = notifications.size() - 1; i >= 0; i--) {
            Notification notification = notifications.get(i);
            if (!blacklist.contains(notification.getTitle())) {
                notification.setContest(contest);
                notificationRepository.save(notifications.get(i));
                publishService.broadcastNotification(notification, contest);
            }
        }
    }
}
