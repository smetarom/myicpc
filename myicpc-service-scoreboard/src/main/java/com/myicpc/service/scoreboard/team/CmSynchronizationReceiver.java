package com.myicpc.service.scoreboard.team;

import com.myicpc.dto.jms.JMSEvent;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.exception.WebServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Receives JMS messages related to participant info
 * <p/>
 * It invokes methods to process the coming messages
 *
 * @author Roman Smetana
 */
@Service
public class CmSynchronizationReceiver {

    @Autowired
    private TeamService teamService;

    @Autowired
    private ContestRepository contestRepository;

    /**
     * Listens to JMS queue {@code QuestNotificationQueue}
     *
     * @param jmsEvent incoming message
     */
    @JmsListener(destination = "java:/jms/queue/CMSynchronizationQueue")
    @Transactional
    public void processQuestEvent(JMSEvent jmsEvent) throws WebServiceException {
        if (jmsEvent == null) {
            return;
        }
        Contest contest = contestRepository.findOne(jmsEvent.getContestId());
        if (contest == null) {
            return;
        }
        switch (jmsEvent.getEventType()) {
            case CM_SYNC_EVENT:
                processCmSyncEvent(contest);
                break;
        }
    }

    private void processCmSyncEvent(Contest contest) throws WebServiceException {
        teamService.synchronizeSocialInfosWithCM(contest);
    }
}
