package com.myicpc.repository.eventFeed;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamRankHistory;
import com.myicpc.repository.AbstractRepositoryTest;
import com.myicpc.repository.contest.ContestRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@Transactional
@DatabaseSetup({"classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/eventFeed/TeamRankHistoryRepositoryTest.xml"})
@DatabaseTearDown("classpath:dbunit/CleanDatabase.xml")
public class TeamRankHistoryRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamRankHistoryRepository teamRankHistoryRepository;

    @Test
    public void testFindByTeam() throws Exception {
        Team team = teamRepository.findOne(1L);
        List<TeamRankHistory> historyList = teamRankHistoryRepository.findByTeam(team);

        Assert.assertEquals(3, historyList.size());
    }

    @Test
    public void testFindByTeamContest() throws Exception {
        Contest contest = contestRepository.findOne(1L);
        List<TeamRankHistory> historyList = teamRankHistoryRepository.findByTeamContest(contest);
        Assert.assertEquals(4, historyList.size());
    }

    @Test
    public void testDeleteByContest() throws Exception {
        Contest contest = contestRepository.findOne(1L);
        Contest contest2 = contestRepository.findOne(2L);
        teamRankHistoryRepository.deleteByContest(contest);
        Assert.assertTrue(teamRankHistoryRepository.findByTeamContest(contest).isEmpty());
        Assert.assertEquals(1, teamRankHistoryRepository.findByTeamContest(contest2).size());
    }
}