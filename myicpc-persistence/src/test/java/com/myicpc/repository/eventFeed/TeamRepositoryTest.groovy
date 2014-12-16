package com.myicpc.repository.eventFeed

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.myicpc.model.contest.Contest
import com.myicpc.model.eventFeed.Team
import com.myicpc.repository.AbstractRepositoryTest
import com.myicpc.repository.contest.ContestRepository
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/eventFeed/TeamRepositoryTest.xml"])
@DatabaseTearDown("classpath:dbunit/CleanDatabase.xml")
public class TeamRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testFindByContest() {
        Contest contest = contestRepository.findOne(1L);
        List<Team> teams = teamRepository.findByContest(contest);
        assert teams.size() == 5
    }

    @Test
    public void testFindByContestAndTeamId() {
        Contest contest = contestRepository.findOne(1L);
        List<Team> teams = teamRepository.findByContestAndTeamIds(contest, [1L, 3L, 5L]);
        assert teams.size() == 3
    }

    @Test
    public void testFindBySystemIdAndContest() {
        Contest contest = contestRepository.findOne(1L);
        Long systemId = 106L;

        Team team = teamRepository.findBySystemIdAndContest(systemId, contest);
        assert team != null
        assert team.getSystemId() == systemId
        assert team.getId() == 4L

    }

    @Test
    public void testDeleteByContest() {
        Contest contest = contestRepository.findOne(1L);
        Contest contest2 = contestRepository.findOne(2L);

        teamRepository.deleteByContest(contest);

        assert teamRepository.findByContest(contest).isEmpty()
        assert teamRepository.findByContest(contest2).size() == 4
    }
}