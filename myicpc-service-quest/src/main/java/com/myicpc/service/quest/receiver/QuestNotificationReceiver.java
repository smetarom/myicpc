package com.myicpc.service.quest.receiver;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.social.Notification;
import com.myicpc.service.quest.QuestMngmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @author Roman Smetana
 */
@Service
public class QuestNotificationReceiver {
    @Autowired
    private QuestMngmService questMngmService;

    @JmsListener(destination = "java:/jms/queue/QuestNotificationQueue")
    public void processQuestNotification(Notification notification) {
        NotificationType notificationType = notification.getNotificationType();
        if (notificationType != null) {
            if (notificationType.isQuestChallenge()) {
                questMngmService.processReceivedNotification(notification);
            }
        }
    }
}
