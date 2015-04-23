package com.myicpc.master.service;

import com.myicpc.model.social.Notification;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Roman Smetana
 */
public abstract class GeneralService {
    private static final Logger logger = LoggerFactory.getLogger(GeneralService.class);

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/SocialNotificationQueue")
    private Queue socialQueue;

    protected void sendSocialNotification(final Notification notification) {

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
        logger.info("Notification send via JMS");
    }

    protected void sendSocialNotifications(final List<Notification> notifications) {
        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(socialQueue);
            connection.start();

            ListIterator<Notification> iterator = notifications.listIterator(notifications.size());
            while (iterator.hasPrevious()) {
                ObjectMessage notificationMessage = session.createObjectMessage(iterator.previous());
                producer.send(notificationMessage);
            }
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
        logger.info("Notification batch (size " + notifications.size() + ") send via JMS");

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
