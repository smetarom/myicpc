package com.myicpc.social.receiver;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.social.Notification;
import com.myicpc.social.service.TwitterService;
import com.myicpc.social.service.VineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @author Roman Smetana
 */
@Service
public class SocialNotificationReceiver {

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private VineService vineService;

    @JmsListener(destination = "java:/jms/queue/SocialNotificationQueue")
    public void processSocialNotification(Notification notification) {
        NotificationType notificationType = notification.getNotificationType();
        if (notificationType != null) {
            if (notificationType.isTwitter()) {
                twitterService.processReceivedNotification(notification);
            } else if (notificationType.isVine()) {
                vineService.processReceivedNotification(notification);
            } else if (notificationType.isQuestChallenge()) {

            }
        }
    }
}
