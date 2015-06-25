package com.myicpc.social.receiver;

import com.myicpc.dto.jms.JMSEvent;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.exception.AuthenticationException;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.social.service.InstagramService;
import com.myicpc.social.service.TwitterService;
import com.myicpc.social.service.VineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Roman Smetana
 */
@Service
public class SocialNotificationReceiver {
    private static final Logger logger = LoggerFactory.getLogger(SocialNotificationReceiver.class);

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private VineService vineService;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private InstagramService instagramService;

    @JmsListener(destination = "java:/jms/queue/SocialNotificationQueue")
    @Transactional
    public void processSocialNotification(JMSEvent jmsEvent) {
        Contest contest = contestRepository.findOne(jmsEvent.getContestId());
        switch (jmsEvent.getEventType()) {
            case VINE:
                processVineUpdates(contest);
                break;
            case INSTAGRAM:
                processInstagramUpdates(contest);
                break;
            case TWITTER:
                processTwitterStart(contest);
                break;
        }
    }

    private void processTwitterStart(Contest contest) {
        twitterService.startTwitterStreaming(contest);
    }

    private void processVineUpdates(Contest contest) {
        try {
            vineService.getNewPosts(contest);
        } catch (Exception e) {
            logger.error("Vine updates failed", e);
        }
    }

    private void processInstagramUpdates(Contest contest) {
        try {
            instagramService.getNewPosts(contest);
        } catch (Exception e) {
            logger.error("Instagram updates failed", e);
        }
    }
}
