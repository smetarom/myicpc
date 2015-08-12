package com.myicpc.service.scoreboard.eventFeed;

import com.google.common.collect.Maps;
import com.myicpc.commons.utils.MessageUtils;
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
import com.myicpc.service.scoreboard.exception.EventFeedException;
import com.myicpc.service.utils.lists.NotificationList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    private final Map<String, Future<Void>> runningFeedProcessors = Maps.newConcurrentMap();

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

    @Transactional
    public void truncateDatabase(Contest contest) throws EventFeedException {
        try {
            lastTeamProblemRepository.deleteByContest(contest);
            teamProblemRepository.deleteByContest(contest);
            teamRankHistoryRepository.deleteByContest(contest);
            eventFeedControlRepository.deleteByContest(contest);
            codeInsightActivityRepository.deleteByContest(contest);
            problemRepository.deleteByContest(contest);
            teamRepository.deleteByContest(contest);
            judgementRepository.deleteByContest(contest);
            List<NotificationType> notificationTypes = NotificationList.newList().addScoreboardSuccess().addScoreboardSubmitted().addScoreboardFailed()
                    .addAnalystMessage();
            notificationRepository.deleteScoreboardNotificationsByContest(contest, notificationTypes);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
            throw new EventFeedException("Error deleting old data from database.");
        }
    }

    /**
     * Starts execution of feed event processing
     */
    public void startEventFeed(Contest contest) throws EventFeedException {
        Future<Void> running = runningFeedProcessors.get(contest.getCode());
        if (running != null && !running.isDone()) {
            throw new EventFeedException(MessageUtils.getMessage("admin.panel.feed.reset.runningThread"));
        }
        Future<Void> newFeedProcessor;
        if (hasContestPollingStrategy(contest)) {
            newFeedProcessor = eventFeedProcessor.pollingEventFeed(contest, 20000);
        } else {
            newFeedProcessor = eventFeedProcessor.runEventFeed(contest);
        }
        runningFeedProcessors.put(contest.getCode(), newFeedProcessor);
    }

    /**
     * Stop event feed
     * <p/>
     * Stops all threads executing event feed, clear database and starts a new
     * thread
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void stopEventFeed(Contest contest) throws EventFeedException {
        Future<Void> running = runningFeedProcessors.get(contest.getCode());
        if (running != null && !running.isDone()) {
            boolean cancelled = running.cancel(true);
            if (!cancelled) {
                throw new EventFeedException(MessageUtils.getMessage("admin.panel.feed.reset.failed"));
            }
        }
    }

    /**
     * Restarts event feed
     * <p/>
     * Stops all threads executing event feed, clear database and starts a new
     * thread
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void restartEventFeed(Contest contest) throws EventFeedException {
        stopEventFeed(contest);
        truncateDatabase(contest);
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
    public void resumeEventFeed(Contest contest)throws EventFeedException {
        stopEventFeed(contest);
        startEventFeed(contest);
    }

    public boolean isEventFeedProcessorRunning(Contest contest) {
        Future<Void> running = runningFeedProcessors.get(contest.getCode());
        return running == null ? false : !running.isDone();
    }

    private boolean hasContestPollingStrategy(final Contest contest) {
        if (contest != null && contest.getContestSettings() != null
                && contest.getContestSettings().getScoreboardStrategyType() != null) {
            return contest.getContestSettings().getScoreboardStrategyType().isPolling();
        }
        return false;
    }


}
