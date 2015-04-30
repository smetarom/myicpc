package com.myicpc.master.bean;

import com.myicpc.master.HATimerService;
import com.myicpc.master.dao.ContestDao;
import com.myicpc.master.service.scoreboard.ScoreboardService;
import com.myicpc.model.contest.Contest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Singleton
public class ScoreboardBean implements IMasterBean {
    private static final Logger logger = LoggerFactory.getLogger(ScoreboardBean.class);

    @Inject
    private ContestDao contestDao;

    @Inject
    private ScoreboardService scoreboardService;

    @Override
    public void initialize() {
        List<Contest> activeContests = contestDao.getActiveContests();
        for (Contest contest : activeContests) {
            System.out.println(contest.getName());
            System.out.println("Start feed");
            scoreboardService.runEventFeed(contest);
            System.out.println("Stop feed");
        }
    }

    @Override
    public void stop() {

    }
}
