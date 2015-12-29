package com.myicpc.service.scoreboard.receiver;

import com.myicpc.commons.utils.WebServiceUtils;
import com.myicpc.dto.jms.JMSEvent;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.scoreboard.exception.CodeInsightException;
import com.myicpc.service.scoreboard.insight.CodeInsightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author Roman Smetana
 */
@Service
public class CodeInsightReceiver {
    private static final Logger logger = LoggerFactory.getLogger(CodeInsightReceiver.class);

    @Autowired
    private CodeInsightService codeInsightService;

    @Autowired
    private ContestRepository contestRepository;

    /**
     * Listens to JMS queue {@code CodeInsightQueue}
     *
     * @param jmsEvent incoming message
     */
    @JmsListener(destination = "java:/jms/queue/CodeInsightQueue")
    @Transactional
    public void processSocialNotification(JMSEvent jmsEvent) {
        Contest contest = contestRepository.findOne(jmsEvent.getContestId());
        if (contest == null) {
            return;
        }
        switch (jmsEvent.getEventType()) {
            case CODE_INSIGHT_EVENT:
                processCodeInsight(contest);
                break;
        }
    }

    private void processCodeInsight(Contest contest) {
        if (contest.getContestSettings() != null && contest.getContestSettings().getEditActivityURL() != null) {
            try {
                String codeInsightResponse = WebServiceUtils.connectAndGetResponse(contest.getContestSettings().getEditActivityURL(),
                    contest.getContestSettings().getEventFeedUsername(),
                    contest.getContestSettings().getEventFeedPassword());
                codeInsightService.processCodeInsightResource(codeInsightResponse, contest);
            } catch (CodeInsightException e) {
                logger.error(e.getMessage(), e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

        }
    }
}
