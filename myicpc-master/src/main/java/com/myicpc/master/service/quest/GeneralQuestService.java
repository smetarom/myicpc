package com.myicpc.master.service.quest;

import com.myicpc.model.social.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * @author Roman Smetana
 */
public abstract class GeneralQuestService {
    private static final Logger logger = LoggerFactory.getLogger(GeneralQuestService.class);

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/QuestNotificationQueue")
    private Queue socialQueue;

    protected void sendQuestNotification(final Notification notification) {

        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(socialQueue);
            connection.start();

            ObjectMessage notificationMessage = session.createObjectMessage(notification);
            producer.send(notificationMessage);
        } catch (JMSException e) {
            logger.error("Social JMS notification not send.", e);
        } finally {
            if (connection != null)   {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }

        // TODO remove log or improve
        logger.info("Quest send via JMS");
    }


}
