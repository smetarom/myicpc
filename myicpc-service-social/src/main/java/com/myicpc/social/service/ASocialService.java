package com.myicpc.social.service;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.BlacklistedUser;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.social.BlacklistedUserRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.publish.PublishService;
import org.apache.commons.lang3.StringUtils;
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
    protected ContestRepository contestRepository;

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
                notificationRepository.save(notification);
                publishService.broadcastNotification(notification, contest);
            }
        }
    }

    protected String createHashtags(String[] tags, String contestHashtag, String questHashtag) {
        StringBuffer tagString = new StringBuffer("|");
        for (String tag : tags) {
            tagString.append(tag).append('|');
        }
        if (tagString.length() > 255) {
            tagString = new StringBuffer("|");
            tagString.append(contestHashtag).append("|");
            if (!StringUtils.isEmpty(questHashtag)) {
                for (String tag : tags) {
                    if (tag.startsWith(questHashtag)) {
                        tagString.append(tag).append('|');
                    }
                }
            }
        }
        return tagString.toString();
    }
}
