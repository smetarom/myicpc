package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.dto.eventFeed.EventFeedCommand;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.stereotype.Service;

import javax.jms.ConnectionFactory;

/**
 * @author Roman Smetana
 */
@Service
public class EJBControlFeedService {
    private static final Logger logger = LoggerFactory.getLogger(EJBControlFeedService.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private DestinationResolver destinationResolver;

    @Autowired
    @Qualifier("eventFeedControlTopic")
    private JmsTemplate jmsTemplate;


    public void stopEventFeed(final Contest contest) throws EventFeedException {
        EventFeedCommand command = new EventFeedCommand(contest.getId(), false, false);
        sendControlCommand(command);
    }

    public void resumeEventFeed(final Contest contest) throws EventFeedException {
        EventFeedCommand command = new EventFeedCommand(contest.getId(), false, true);
        sendControlCommand(command);
    }

    public void restartEventFeed(final Contest contest) throws EventFeedException {
        EventFeedCommand command = new EventFeedCommand(contest.getId(), true, true);
        sendControlCommand(command);
    }

    public void clearEventFeed(final Contest contest) throws EventFeedException {
        EventFeedCommand command = new EventFeedCommand(contest.getId(), true, false);
        sendControlCommand(command);
    }

    private void sendControlCommand(final EventFeedCommand command) {

        jmsTemplate.convertAndSend(command);

//        jmsTemplate.send(new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                ObjectMessage message = session.createObjectMessage(command);
//                return message;
//            }
//        });


//        Connection connection = null;

//        try {
//            connection = connectionFactory.createConnection();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//            Destination feedControlTopic = destinationResolver.resolveDestinationName(session, "java:/jms/topic/EventFeedControlTopic", true);
//            MessageProducer producer = session.createProducer(feedControlTopic);
//
//            Destination tempDest = session.createTemporaryQueue();
//            MessageConsumer responseConsumer = session.createConsumer(tempDest);
//
//            connection.start();
//
//            ObjectMessage notificationMessage = session.createObjectMessage(command);
//            producer.send(notificationMessage);
//
//            Message response = responseConsumer.receive();
//            System.out.println(response);
//        } catch (JMSException e) {
//            logger.error("Event feeed JMS notification not send.", e);
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (JMSException e) {
//                }
//            }
//        }
    }
}
