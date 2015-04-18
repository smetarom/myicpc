package com.myicpc.master;

import com.myicpc.master.dao.ContestDao;
import com.myicpc.master.service.TwitterService;
import com.myicpc.model.contest.Contest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * A simple example to demonstrate a implementation of a cluster-wide singleton timer.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Singleton
public class SocialSchedulerBean implements SocialScheduler {
    private static final Logger logger = LoggerFactory.getLogger(HATimerService.class);
    @Resource
    private TimerService timerService;

    @Inject
    private ContestDao contestDao;

    @Inject
    private TwitterService twitterService;

    @Timeout
    public void scheduler(Timer timer) {
        logger.info("HASingletonTimer: Info=" + timer.getInfo());
    }

    @Override
    public void initialize(String info) {
        List<Contest> activeContests = contestDao.getActiveContests();
        for (Contest contest : activeContests) {
            twitterService.startTwitterStreaming(contest);
        }

        ScheduleExpression sexpr = new ScheduleExpression();
        // set schedule to every 10 seconds for demonstration
        sexpr.hour("*").minute("*").second("0/10");
        // persistent must be false because the timer is started by the HASingleton service
        timerService.createCalendarTimer(sexpr, new TimerConfig(info, false));
    }

    @Override
    public void stop() {
        logger.info("Stop all existing HASingleton timers");
        for (Timer timer : timerService.getTimers()) {
            logger.trace("Stop HASingleton timer: " + timer.getInfo());
            timer.cancel();
        }
    }
}
