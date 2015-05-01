package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.dto.eventFeed.XMLEntity;
import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.contest.ContestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @author Roman Smetana
 */
@Service
public class EventFeedNotificationReceiver {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedNotificationReceiver.class);

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private EventFeedVisitor eventFeedVisitor;

    @JmsListener(destination = "java:/jms/queue/EventFeedQueue")
    public void processSocialNotification(XMLEntity<?> xmlEntity) {
        if (xmlEntity.getContestId() == null) {
            logger.error("Received event feed entity without contest ID: " + xmlEntity);
        }
        Contest contest = contestRepository.findOne(xmlEntity.getContestId());
        xmlEntity.accept(eventFeedVisitor, contest);
    }
}
