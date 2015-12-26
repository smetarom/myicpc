package com.myicpc.service.scoreboard.eventFeed;

import com.google.common.collect.Maps;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.codeInsight.CodeInsightActivityRepository;
import com.myicpc.repository.eventFeed.EventFeedControlRepository;
import com.myicpc.repository.eventFeed.JudgementRepository;
import com.myicpc.repository.eventFeed.LastTeamProblemRepository;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRankHistoryRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.scoreboard.dto.eventFeed.EventFeedControlDTO;
import com.myicpc.service.scoreboard.dto.eventFeed.EventFeedControlDTO.EventFeedControlType;
import com.myicpc.service.scoreboard.dto.eventFeed.EventFeedControlResponseDTO;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import com.myicpc.service.utils.lists.NotificationList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Service that provides business logic for stopping, starting, restarting and
 * reconnection to Event feed
 *
 * @author Roman Smetana
 */
@Service
public class ControlFeedService {
    private static final Logger logger = LoggerFactory.getLogger(ControlFeedService.class);
    private static final String EVENT_FEED_CONTROL_TOPIC = "java:/jms/topic/EventFeedControlTopic";
    public static final int DEFAULT_EVENT_FEED_TIMEOUT = 5 * 1000; // 5 seconds to milliseconds

    /**
     * Map between {@link Contest#id} and its {@link RunningFeedProcessDTO}
     */
    private static final Map<Long, RunningFeedProcessDTO> runningFeedProcesses = Maps.newConcurrentMap();

    @Autowired
    private EventFeedProcessor eventFeedProcessor;

    @Autowired
    private JudgementRepository judgementRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamProblemRepository teamProblemRepository;

    @Autowired
    private LastTeamProblemRepository lastTeamProblemRepository;

    @Autowired
    private CodeInsightActivityRepository codeInsightActivityRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TeamRankHistoryRepository teamRankHistoryRepository;

    @Autowired
    private EventFeedControlRepository eventFeedControlRepository;

    @Autowired
    @Qualifier("jmsTopicTemplate")
    private JmsTemplate jmsTopicTemplate;

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsQueueTemplate;

    @Transactional
    private void truncateDatabase(Contest contest, boolean deleteNotifications) throws EventFeedException {
        try {
            lastTeamProblemRepository.deleteByContest(contest);
            teamProblemRepository.deleteByContest(contest);
            teamRankHistoryRepository.deleteByContest(contest);
            eventFeedControlRepository.deleteByContest(contest);
            codeInsightActivityRepository.deleteByContest(contest);
            problemRepository.deleteByContest(contest);
            teamRepository.deleteByContest(contest);
            judgementRepository.deleteByContest(contest);
            if (deleteNotifications) {
                List<NotificationType> notificationTypes = NotificationList.newList().addScoreboardSuccess().addScoreboardSubmitted().addScoreboardFailed()
                        .addAnalystMessage();
                notificationRepository.deleteScoreboardNotificationsByContest(contest, notificationTypes);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            throw new EventFeedException("Error deleting old data from database.");
        }
    }

    /**
     * Starts execution of feed event processing
     *
     * @param contest contest
     */
    public void startEventFeed(Contest contest) {
        Future<Void> newFeedProcess;
        if (hasContestPollingStrategy(contest)) {
            newFeedProcess = eventFeedProcessor.pollingEventFeed(contest, 20000);
        } else {
            newFeedProcess = eventFeedProcessor.runEventFeed(contest);
        }
        runningFeedProcesses.put(contest.getId(), new RunningFeedProcessDTO(newFeedProcess, new Date()));
    }

    /**
     * Stops the event feed processing
     * <p/>
     * It sends command to all instances subscribing to {@link #EVENT_FEED_CONTROL_TOPIC}
     * and ask them to cancel the event feed processing
     * <p/>
     * It waits till the instance, which runs the event feed, terminates the event feed or timeout
     *
     * @param contest contest
     */
    public void stopEventFeed(final Contest contest) {
        final EventFeedControlDTO eventFeedControl =
                new EventFeedControlDTO(EventFeedControlType.STOP, new Date(), contest.getId());
        jmsTopicTemplate.setReceiveTimeout(DEFAULT_EVENT_FEED_TIMEOUT);
        // block until the running event feed terminates and responds or till timeout
        jmsTopicTemplate.sendAndReceive(EVENT_FEED_CONTROL_TOPIC, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(eventFeedControl);
            }
        });
    }

    /**
     * Listen on {@link #EVENT_FEED_CONTROL_TOPIC} and executes incoming commands
     * <p/>
     * It processes the event stop action, where it looks up if it has a event feed process running for contest,
     * which started before the STOP command was created. And if so, it cancels the running job and sends
     * action response about termination
     * <p/>
     * It processes the event feed status command. It looks up if it has a event feed process running for contest.
     * And if so, it sends action response about the process state.
     *
     * @param eventFeedControl event feed command
     * @param responseQueue    temporary response {@link Queue}
     */
    @JmsListener(destination = EVENT_FEED_CONTROL_TOPIC, containerFactory = "jmsTopicListenerContainerFactory")
    public void processEventFeedControl(EventFeedControlDTO eventFeedControl, @Header(value = "jms_replyTo", required = false) Queue responseQueue) {
        if (eventFeedControl == null) {
            return;
        }
        RunningFeedProcessDTO runningFeedProccesor = getRunningFeedProcessor(eventFeedControl.getContestId(), eventFeedControl.getSubmittedDate());
        if (runningFeedProccesor != null && runningFeedProccesor.isRunning()) {
            final EventFeedControlResponseDTO response;
            if (eventFeedControl.getEventFeedControlType() == EventFeedControlType.STOP) {
                boolean cancelled = runningFeedProccesor.getJob().cancel(true);
                if (!cancelled) {
                    logger.error("Stopping event feed for contest {} failed.", eventFeedControl.getContestId());
                }
                response = new EventFeedControlResponseDTO(EventFeedControlType.STOP);
            } else if (eventFeedControl.getEventFeedControlType() == EventFeedControlType.STATUS) {
                response = new EventFeedControlResponseDTO(EventFeedControlType.STATUS);
            } else {
                response = null;
            }
            if (responseQueue != null && response != null) {
                jmsQueueTemplate.send(responseQueue, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(response);
                    }
                });
            }
        }
    }

    /**
     * Restarts event feed
     * <p/>
     * Stops all threads executing event feed, clear database and starts a new
     * thread
     *
     * @throws EventFeedException database clear failed
     */
    public void restartEventFeed(Contest contest) throws EventFeedException {
        stopEventFeed(contest);
        truncateDatabase(contest, true);
        startEventFeed(contest);
    }

    /**
     * Reconnects to the feed
     * <p/>
     * Stops all threads executing event feed, leave database and starts a new
     * thread
     *
     * @throws InterruptedException
     * @throws IOException
     */
    public void resumeEventFeed(Contest contest) throws EventFeedException {
        stopEventFeed(contest);
        startEventFeed(contest);
    }

    /**
     * Clear data from event feed
     * <p/>
     * Stops all threads executing event feed and clear database
     *
     * @throws EventFeedException database clear failed
     */
    public void clearEventFeed(Contest contest) throws EventFeedException {
        stopEventFeed(contest);
        truncateDatabase(contest, false);
    }

    /**
     * It gets if the event feed is running on any instance, which subscribes
     * {@link #EVENT_FEED_CONTROL_TOPIC}
     *
     * @param contest contest
     * @return is event feed process running at the moment
     */
    public boolean isEventFeedProcessorRunning(Contest contest) {
        final EventFeedControlDTO eventFeedControl =
                new EventFeedControlDTO(EventFeedControlType.STATUS, new Date(), contest.getId());
        jmsTopicTemplate.setReceiveTimeout(DEFAULT_EVENT_FEED_TIMEOUT);
        // block until gets the event feed status or till timeout
        Message message = jmsTopicTemplate.sendAndReceive(EVENT_FEED_CONTROL_TOPIC, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(eventFeedControl);
            }
        });
        return message != null;
    }

    public void uploadEventFeed(InputStream eventFeedStream, Contest contest) throws EventFeedException {
        stopEventFeed(contest);
        truncateDatabase(contest, false);
        eventFeedProcessor.uploadEventFeed(eventFeedStream, contest);
    }

    /**
     * Checks if the contest has a polling strategy for event feed selected
     *
     * @param contest contest
     * @return true, if the contest has polling strategy, false for other strategies
     */
    public static boolean hasContestPollingStrategy(final Contest contest) {
        return contest != null &&
                contest.getContestSettings() != null &&
                contest.getContestSettings().getScoreboardStrategyType() != null &&
                contest.getContestSettings().getScoreboardStrategyType().isPolling();
    }

    private static RunningFeedProcessDTO getRunningFeedProcessor(Long contestId, Date untilDate) {
        RunningFeedProcessDTO runningFeedProcess = runningFeedProcesses.get(contestId);
        if (runningFeedProcess != null && runningFeedProcess.getCreated().before(untilDate)) {
            return runningFeedProcess;
        }
        return null;
    }

    /**
     * Job data holder
     * <p/>
     * It holds the link to running event feed process and the date, when the job has started
     */
    private static class RunningFeedProcessDTO {
        private final Future<Void> job;
        private final Date created;

        private RunningFeedProcessDTO(Future<Void> job, Date created) {
            this.job = job;
            this.created = created;
        }

        public Future<Void> getJob() {
            return job;
        }

        public Date getCreated() {
            return created;
        }

        public boolean isRunning() {
            return job != null && !job.isDone();
        }
    }


}
