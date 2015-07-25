package com.myicpc.service.quest.receiver;

import com.myicpc.dto.jms.JMSEvent;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.quest.QuestMngmService;
import com.myicpc.service.quest.QuestSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Receives JMS messages related to Quest
 * <p/>
 * It invokes methods to process the coming messages
 *
 * @author Roman Smetana
 */
@Service
public class QuestNotificationReceiver {
    @Autowired
    private QuestMngmService questMngmService;

    @Autowired
    private QuestSubmissionService questSubmissionService;

    @Autowired
    private ContestRepository contestRepository;

    /**
     * Listens to JMS queue {@code QuestNotificationQueue}
     *
     * @param jmsEvent incoming message
     */
    @JmsListener(destination = "java:/jms/queue/QuestNotificationQueue")
    @Transactional
    public void processQuestEvent(JMSEvent jmsEvent) {
        if (jmsEvent == null) {
            return;
        }
        Contest contest = contestRepository.findOne(jmsEvent.getContestId());
        if (contest == null) {
            return;
        }
        switch (jmsEvent.getEventType()) {
            case QUEST_CHALLENGE:
                processQuestChallengeUpdates(contest);
                break;
            case QUEST_SUBMISSIONS:
                processQuestSubmissionUpdates(contest);
                break;
            case QUEST_SUBMISSIONS_FULL:
                processQuestSubmissionUpdatesFull(contest);
                break;
        }
    }

    private void processQuestSubmissionUpdatesFull(Contest contest) {
        questSubmissionService.processAllSubmissions(contest);
    }

    private void processQuestSubmissionUpdates(Contest contest) {
        questSubmissionService.processRecentSubmissions(contest);
    }

    private void processQuestChallengeUpdates(Contest contest) {
        questMngmService.createNotificationsForNewQuestChallenges(contest);
    }
}
