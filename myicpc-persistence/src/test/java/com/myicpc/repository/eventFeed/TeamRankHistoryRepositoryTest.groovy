package com.myicpc.repository.eventFeed

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.myicpc.model.contest.Contest
import com.myicpc.model.eventFeed.Team
import com.myicpc.model.eventFeed.TeamRankHistory
import com.myicpc.repository.AbstractRepositoryTest
import com.myicpc.repository.contest.ContestRepository
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/eventFeed/TeamRankHistoryRepositoryTest.xml"])
@DatabaseTearDown("classpath:dbunit/CleanDatabase.xml")
public class TeamRankHistoryRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamRankHistoryRepository teamRankHistoryRepository;

    @Test
    public void testFindByTeam() {
        Team team = teamRepository.findOne(1L);
        List<TeamRankHistory> historyList = teamRankHistoryRepository.findByTeam(team);
        assert historyList.size() == 3
    }

    @Test
    public void testFindByTeamContest() {
        Contest contest = contestRepository.findOne(1L);
        List<TeamRankHistory> historyList = teamRankHistoryRepository.findByTeamContest(contest);
        assert historyList.size() == 4
    }

    @Test
    public void testDeleteByContest() {
        Contest contest = contestRepository.findOne(1L);
        Contest contest2 = contestRepository.findOne(2L);
        teamRankHistoryRepository.deleteByContest(contest);
        assert teamRankHistoryRepository.findByTeamContest(contest).isEmpty()
        assert teamRankHistoryRepository.findByTeamContest(contest2).size() == 1
    }
}