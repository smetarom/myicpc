package com.myicpc.service.scoreboard.eventFeed;

import com.google.common.collect.Maps;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.editActivity.EditActivityRepository;
import com.myicpc.repository.eventFeed.*;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.utils.lists.NotificationList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
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
@Transactional
public class ControlFeedService {
    private static final Logger logger = LoggerFactory.getLogger(ControlFeedService.class);

    private Map<String, Future<Void>> runningFeedProcessors = Maps.newConcurrentMap();

    @Autowired
    private EventFeedProcessor eventFeedProcessor;

    @Autowired
    private RegionRepository regionRepository;

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
    private EditActivityRepository editActivityRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public void truncateDatabase(Contest contest) {
        lastTeamProblemRepository.deleteByContest(contest);
        teamProblemRepository.deleteByContest(contest);
        editActivityRepository.deleteByContest(contest);
        problemRepository.deleteByContest(contest);
        teamRepository.deleteByContest(contest);
        regionRepository.deleteByContest(contest);
        judgementRepository.deleteByContest(contest);
        List<Notification.NotificationType> notificationTypes = NotificationList.newList().addScoreboardSuccess().addScoreboardSubmitted().addScoreboardFailed()
                .addAnalystMessage();
        notificationRepository.deleteScoreboardNotificationsByContest(contest, notificationTypes);
    }

    /**
     * Starts execution of feed event processing
     */
    public void startFeed(Contest contest) {
        Future<Void> newFeedProcessor = eventFeedProcessor.runEventFeed(contest);
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
    public void stopFeed(Contest contest) throws IOException, InterruptedException {
        Future<Void> running = runningFeedProcessors.get(contest.getCode());
        if (running != null && !running.isDone()) {
            boolean cancelled = running.cancel(true);
            // TODO handle return false
            System.out.println();
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
    public void resetFeed(Contest contest) throws IOException, InterruptedException {
        stopFeed(contest);
        truncateDatabase(contest);
        startFeed(contest);
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
    public void reconnectFeed(Contest contest) throws InterruptedException, IOException {
        stopFeed(contest);
        startFeed(contest);
    }

    public boolean isEventFeedProcessorRunning(Contest contest) {
        Future<Void> running = runningFeedProcessors.get(contest.getCode());
        return running == null ? false : !running.isDone();
    }

    /**
     * Find threads called 'feedThread'
     *
     * @return threads called 'feedThread'
     */
    public List<Thread> findXMLFeedThread() {
        return findThread("feedThread");
    }

    /**
     * Find thread by name
     *
     * @param threadName thread name
     * @return All threads with the given name
     */
    public List<Thread> findThread(final String threadName) {
        List<Thread> threads = new ArrayList<Thread>();
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (threadName.equalsIgnoreCase(t.getName())) {
                threads.add(t);
            }
        }
        return threads;
    }

    /**
     * Interrupt a thead and waits until it finishes
     *
     * @param t thread to be stopped
     * @throws InterruptedException
     */
    public void stopThread(final Thread t) throws InterruptedException {
        logger.info(t.toString() + " - " + t.isInterrupted());
        t.interrupt();
        logger.info(t.toString() + " - " + t.isInterrupted());
        t.join();
        logger.info(t.toString() + " - " + t.isAlive());
    }
}
