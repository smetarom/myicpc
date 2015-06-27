package com.myicpc.service.quest.receiver;

import com.myicpc.dto.jms.JMSEvent;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.quest.QuestMngmService;
import com.myicpc.service.quest.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Roman Smetana
 */
@Service
public class QuestNotificationReceiver {
    @Autowired
    private QuestService questService;

    @Autowired
    private ContestRepository contestRepository;

    @JmsListener(destination = "java:/jms/queue/QuestNotificationQueue")
    @Transactional
    public void processQuestNotification(JMSEvent jmsEvent) {
        Contest contest = contestRepository.findOne(jmsEvent.getContestId());
        if (contest == null) {
            return;
        }
        switch (jmsEvent.getEventType()) {
            case QUEST_CHALLENGE:
                processQuestChallengeUpdates(contest);
                break;
        }
    }

    private void processQuestChallengeUpdates(Contest contest) {
        questService.createNotificationsForNewQuestChallenges(contest);
    }
}
