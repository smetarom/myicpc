package com.myicpc.master.service.scoreboard;

import com.myicpc.dto.eventFeed.EventFeedCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * @author Roman Smetana
 */
//@MessageDriven(name = "EventFeedControlTopic", activationConfig = {
//        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/topic/EventFeedControlTopic"),
//        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
//        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class FeedControlReceiver implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(FeedControlReceiver.class);

    @EJB
    private EventFeedService eventFeedService;

    @Override
    public void onMessage(Message rcvMessage) {
        try {
            if (rcvMessage instanceof ObjectMessage) {
                Serializable rcvObject = ((ObjectMessage) rcvMessage).getObject();
                if (rcvObject instanceof EventFeedCommand) {
                    EventFeedCommand command = (EventFeedCommand) rcvObject;
                    logger.info("Received Message from topic: " + command);
                    eventFeedService.processReceivedCommand((ObjectMessage) rcvMessage, command);
                } else {
                    logger.error("Message of wrong instance: " + rcvObject.getClass().getName());
                }
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
