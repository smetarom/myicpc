package com.myicpc.master.bean;

import com.myicpc.master.dao.ContestDao;
import com.myicpc.master.service.scoreboard.EventFeedService;
import com.myicpc.model.contest.Contest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Roman Smetana
 */
@Singleton(name = "ScoreboardBean")
@Local(IMasterBean.class)
public class ScoreboardBean implements IMasterBean {
    private static final Logger logger = LoggerFactory.getLogger(ScoreboardBean.class);

    private final AtomicBoolean started = new AtomicBoolean(false);

    @Inject
    private ContestDao contestDao;

    @Inject
    private EventFeedService eventFeedService;

    @Override
    public void initialize() {
        List<Contest> activeContests = contestDao.getActiveContests();
        for (Contest contest : activeContests) {
            System.out.println(contest.getName());
            eventFeedService.startEventFeed(contest);
        }
        started.set(true);
    }

    @Override
    public void stop() {
        started.set(false);
    }

    @Override
    public AtomicBoolean getStarted() {
        return started;
    }
}
