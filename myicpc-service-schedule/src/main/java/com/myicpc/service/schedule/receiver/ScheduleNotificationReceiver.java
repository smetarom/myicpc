package com.myicpc.service.schedule.receiver;


import com.myicpc.dto.jms.JMSEvent;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.schedule.ScheduleMngmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Receives JMS messages related to schedule
 * <p/>
 * It invokes methods to process the coming messages
 *
 * @author Roman Smetana
 */
@Service
public class ScheduleNotificationReceiver {
    @Autowired
    private ScheduleMngmService scheduleMngmService;

    @Autowired
    private ContestRepository contestRepository;


    /**
     * Listens to JMS queue {@code ScheduleNotificationQueue}
     *
     * @param jmsEvent incoming message
     */
    @JmsListener(destination = "java:/jms/queue/ScheduleNotificationQueue")
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
            case SCHEDULE_OPEN_EVENT:
                processNewOpenEvent(contest);
                break;
        }
    }

    private void processNewOpenEvent(Contest contest) {
        scheduleMngmService.createNonPublishedEventNotifications(contest);
    }
}
