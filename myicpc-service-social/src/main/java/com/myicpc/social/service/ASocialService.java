package com.myicpc.social.service;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.BlacklistedUser;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.social.BlacklistedUserRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.publish.PublishService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Parent service for all services working with social networks
 *
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
     * @param notifications       list of results
     * @param blacklistedUserType type of blacklist service
     * @param contest             contest
     */
    protected void saveSearchList(List<Notification> notifications, BlacklistedUser.BlacklistedUserType blacklistedUserType, Contest contest) {
        if (CollectionUtils.isEmpty(notifications)) {
            return;
        }
        Set<String> blacklist = new HashSet<>(blacklistedUserRepository.getUsernameByBlacklistedUserType(blacklistedUserType));

        for (int i = notifications.size() - 1; i >= 0; i--) {
            Notification notification = notifications.get(i);
            if (!blacklist.contains(notification.getAuthorUsername())) {
                notification.setContest(contest);
                notificationRepository.save(notification);
                publishService.broadcastNotification(notification, contest);
            }
        }
    }

    /**
     * Encodes post hashtags into application representation
     *
     * @param tags list of hashtags
     * @param contestHashtag contest hashtag
     * @param questHashtagPrefix Quest hashtag prefix
     * @return
     */
    protected String createHashtags(String[] tags, String contestHashtag, String questHashtagPrefix) {
        StringBuffer tagString = new StringBuffer("|");
        for (String tag : tags) {
            tagString.append(tag).append('|');
        }
        if (tagString.length() > 2048) {
            tagString = new StringBuffer("|");
            tagString.append(contestHashtag).append("|");
            if (!StringUtils.isEmpty(questHashtagPrefix)) {
                for (String tag : tags) {
                    if (tag.startsWith(questHashtagPrefix)) {
                        tagString.append(tag).append('|');
                    }
                }
            }
        }
        return tagString.toString();
    }
}
