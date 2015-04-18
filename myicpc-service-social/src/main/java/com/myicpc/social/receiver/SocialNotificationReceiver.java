package com.myicpc.social.receiver;

import com.myicpc.model.social.Notification;
import com.myicpc.social.service.TwitterService;
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

    @JmsListener(destination = "java:/jms/queue/SocialNotificationQueue")
    public void processSocialNotification(Notification notification) {
        if (notification.getNotificationType() != null) {
            if (notification.getNotificationType().isTwitter()) {
                twitterService.processReceivedNotification(notification);
            }
        }
    }
}
