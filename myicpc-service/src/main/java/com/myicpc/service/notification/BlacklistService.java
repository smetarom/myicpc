package com.myicpc.service.notification;

import com.google.common.collect.HashBiMap;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.BlacklistedUser;
import com.myicpc.model.social.BlacklistedUser.BlacklistedUserType;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.BlacklistedUserRepository;
import com.myicpc.repository.social.NotificationRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This service defines methods to manage {@link BlacklistedUser} and
 * {@link BadWordsFilter}
 *
 * @author Roman Smetana
 */
@Service
@Transactional
public class BlacklistService {
    private static final HashBiMap<NotificationType, BlacklistedUserType> fromNotificationToBlacklist;

    @Autowired
    private BlacklistedUserRepository blacklistedUserRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    static {
        fromNotificationToBlacklist = HashBiMap.create();
        fromNotificationToBlacklist.put(NotificationType.TWITTER, BlacklistedUserType.TWITTER);
        fromNotificationToBlacklist.put(NotificationType.VINE, BlacklistedUserType.VINE);
        fromNotificationToBlacklist.put(NotificationType.INSTAGRAM, BlacklistedUserType.INSTAGRAM);
        fromNotificationToBlacklist.put(NotificationType.YOUTUBE_VIDEO, BlacklistedUserType.YOUTUBE);
    }

    /**
     * Add user identified by username and type of social media
     *
     * @param username
     *            username of the user
     * @param type
     *            associated social media type
     */
    public void addUsernameToBlacklist(String username, final BlacklistedUserType type, final Contest contest) {
        if (BlacklistedUserType.TWITTER == type) {
            if (!StringUtils.isEmpty(username) && username.charAt(0) == '@') {
                username = username.substring(1);
            }
        }

        BlacklistedUser blacklistedUser = blacklistedUserRepository.findByUsernameAndBlacklistedUserType(username, type);
        if (blacklistedUser == null) {
            blacklistedUser = new BlacklistedUser();
            blacklistedUser.setUsername(username);
            blacklistedUser.setSocialMediaUserType(type);
            blacklistedUser.setContest(contest);

            blacklistedUserRepository.save(blacklistedUser);
        }
        markDeletedRecordsByUsername(username, type, contest);
    }

    /**
     * Highligh swear words by wrapping them into HTML tag with class
     * "suspicious"
     *
     * @param notification
     *            checked notification
     */
    public void hightlightSwearWords(final Notification notification) {
        Set<String> swearWords = BadWordsFilter.getSwearWords(notification);
        notification.setHighlightedBody(notification.getBody());
        StringBuilder highlightedBody = new StringBuilder(notification.getBody());
        for (String word : swearWords) {
            String regexp = "(";
            for (int i = 0; i < word.length() - 1; i++) {
                regexp += word.charAt(i) + "\\P{Alnum}*";
            }
            regexp += word.charAt(word.length() - 1) + ")";
            Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(highlightedBody.toString());
            int offset = 0;
            while (matcher.find()) {
                highlightedBody
                        .replace(matcher.start() + offset, matcher.end() + offset, "<span class=\"suspicious\">" + matcher.group() + "</span>");
                offset += 32;
            }
        }
        notification.setHighlightedBody(highlightedBody.toString());
    }

    /**
     * Ban author of the notification
     *
     * Banning includes moving notification author to blacklist and removing all
     * his posts from the system
     *
     * @param notification
     *            inappropriate notification
     */
    public void banAuthor(final Notification notification) {
        if (StringUtils.isNotEmpty(notification.getAuthorName()) && notification.getNotificationType() != null) {
            BlacklistedUserType blacklistedUserType = fromNotificationToBlacklist.get(notification.getNotificationType());
            addUsernameToBlacklist(notification.getAuthorName(), blacklistedUserType, notification.getContest());
        }
    }

    /**
     * Remove all posts from the user from given social media
     *
     * @param username
     *            username identifying the user
     * @param type
     *            social media type
     */
    private void markDeletedRecordsByUsername(final String username, final BlacklistedUserType type, final Contest contest) {
        if (type == null || StringUtils.isEmpty(username)) {
            return;
        }
        NotificationType notificationType = fromNotificationToBlacklist.inverse().get(type);
        if (notificationType != null) {
            List<Notification> notifications = notificationRepository.findByAuthorUsernameAndNotificationTypeAndContest(username, notificationType, contest);
            if (notificationType.isTwitter()) {
                List<Long> notificationIds = new ArrayList<>();
                for (Notification notification : notifications) {
                    notificationIds.add(notification.getId());
                }
                if (!notificationIds.isEmpty()) {
                    List<Notification> children = notificationRepository.findByIdIn(notificationIds);
                    notificationService.markDeleted(children);
                }
            }
            notificationService.markDeleted(notifications);
        }
    }

    /**
     * Delete a single notification
     *
     * @param notification
     *            notification to delete
     */
    public void markPostDeleted(final Notification notification) {
        if (notification.getNotificationType() != null) {
            if (notification.getNotificationType().isTwitter()) {
                List<Notification> children = notificationRepository.findByParentIdAndNotificationTypeAndContest(notification.getId(), notification.getNotificationType(), notification.getContest());
                for (Notification child : children) {
                    child.setDeleted(true);
                }
                notificationRepository.save(children);
            }
        }
        notificationService.markDeleted(notification);
    }
}
