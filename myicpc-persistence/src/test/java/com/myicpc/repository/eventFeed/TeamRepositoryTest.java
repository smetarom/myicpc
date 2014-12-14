package com.myicpc.repository.eventFeed;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.google.common.collect.Lists;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.repository.AbstractRepositoryTest;
import com.myicpc.repository.contest.ContestRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@Transactional
@DatabaseSetup("classpath:dbunit/TeamRepositoryTest.xml")
public class TeamRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testFindByContest() throws Exception {
        Contest contest = contestRepository.findOne(1L);

        List<Team> teams = teamRepository.findByContest(contest);
        Assert.assertEquals(5, teams.size());
    }

    @Test
    public void testFindByContestAndTeamId() throws Exception {
        List<Long> ids = Lists.newArrayList(1L, 3L, 5L);
        Contest contest = contestRepository.findOne(1L);

        List<Team> teams = teamRepository.findByContestAndTeamIds(contest, ids);
        Assert.assertEquals(3, teams.size());
    }

    @Test
    public void testFindBySystemIdAndContest() throws Exception {
        Contest contest = contestRepository.findOne(1L);
        Long systemId = 106L;

        Team team = teamRepository.findBySystemIdAndContest(systemId, contest);
        Assert.assertNotNull(team);
        Assert.assertEquals(106L, (long) team.getSystemId());
        Assert.assertEquals(4L, (long) team.getId());

    }

    @Test
    public void testDeleteByContest() throws Exception {
        Contest contest = contestRepository.findOne(1L);
        Contest contest2 = contestRepository.findOne(2L);

        teamRepository.deleteByContest(contest);

        Assert.assertTrue(teamRepository.findByContest(contest).isEmpty());
        Assert.assertEquals(4, teamRepository.findByContest(contest2).size());
    }
}