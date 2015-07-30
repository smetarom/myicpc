package com.myicpc.repository.eventFeed

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.myicpc.model.contest.Contest
import com.myicpc.model.eventFeed.Problem
import com.myicpc.model.eventFeed.Team
import com.myicpc.model.eventFeed.TeamProblem
import com.myicpc.repository.AbstractRepositoryTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

/**
 * @author Roman Smetana
 */
@Transactional
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/eventFeed/LastTeamProblemRepositoryTest.xml"])
@DatabaseTearDown("classpath:dbunit/CleanDatabase.xml")
class TeamProblemRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamProblemRepository teamProblemRepository;

    @Test
    void testFindBySystemIdAndTeamContest() {
        Contest contest = contestRepository.findOne(1L);
        TeamProblem teamProblem = teamProblemRepository.findBySystemIdAndTeamContest(22L, contest);
        assert teamProblem != null
        assert teamProblem != 3
    }

    @Test
    void testFindByTeam() {
        Team team = teamRepository.findOne(1L);
        List<TeamProblem> teamProblemList = teamProblemRepository.findByTeam(team);
        assert teamProblemList.size() == 4
        assert teamProblemList[0].getId() in [1L, 4L, 11L, 12L]
        assert teamProblemList[1].getId() in [1L, 4L, 11L, 12L]
    }

    @Test
    void testFindByTeamOrderByTimeDesc() {
        Team team = teamRepository.findOne(1L);
        List<TeamProblem> teamProblemList = teamProblemRepository.findByTeamOrderByTimeDesc(team);
        assert teamProblemList.size() == 4
        assert teamProblemList[0].getId() == 12
        assert teamProblemList[1].getId() == 11
        assert teamProblemList[2].getId() == 1
        assert teamProblemList[3].getId() == 4
    }

    @Test
    void testFindByProblem() {
        Problem problem = problemRepository.findOne(1L);
        List<TeamProblem> teamProblemList = teamProblemRepository.findByProblem(problem);
        assert teamProblemList.size() == 5;
        assert teamProblemList[0].getId() in [1L, 2L, 3L]
        assert teamProblemList[1].getId() in [1L, 2L, 3L]
        assert teamProblemList[2].getId() in [1L, 2L, 3L]
    }

    @Test
    void testFindByProblemAndFirstSolved_True() {
        Problem problem = problemRepository.findOne(1L);
        List<TeamProblem> teamProblemList = teamProblemRepository.findByProblemAndFirstSolved(problem, true);
        assert teamProblemList.size() == 1;
        assert teamProblemList[0].getId() == 3
    }

    @Test
    void testFindByProblemAndFirstSolved_False() {
        Problem problem = problemRepository.findOne(1L);
        List<TeamProblem> teamProblemList = teamProblemRepository.findByProblemAndFirstSolved(problem, false);
        assert teamProblemList.size() == 4;
        assert teamProblemList[0].getId() in [1L, 2L, 11L, 12L]
        assert teamProblemList[1].getId() in [1L, 2L, 11L, 12L]
        assert teamProblemList[2].getId() in [1L, 2L, 11L, 12L]
        assert teamProblemList[3].getId() in [1L, 2L, 11L, 12L]
    }

    @Test
    void testCountTeamProblemsByTeamAndProblem() {
        Team team = teamRepository.findOne(1L);
        Problem problem = problemRepository.findOne(1L);
        Long count = teamProblemRepository.countTeamProblemsByTeamAndProblem(team, problem);
        assert count == 3
    }

    @Test
    void testFindByTeamAndProblemOrderByTimeAsc() {
        Team team = teamRepository.findOne(1L);
        Problem problem = problemRepository.findOne(1L);
        List<TeamProblem> teamProblemList = teamProblemRepository.findByTeamAndProblemOrderByTimeAsc(team, problem);
        assert teamProblemList.size() == 3
        assert teamProblemList[0].getId() == 1
        assert teamProblemList[1].getId() == 11
        assert teamProblemList[2].getId() == 12
    }

    @Test
    void testGetLastAcceptedTeamProblemTime() {
        Team team = teamRepository.findOne(1L);
        Double time = teamProblemRepository.getLastAcceptedTeamProblemTime(team);
        assert time == 114.35902
    }

    @Test
    void testDeleteByContest() {
        // TODO fix me
//        Contest contest2 = contestRepository.findOne(2L);
//
//        teamProblemRepository.deleteByContest(contest2);
//        List<TeamProblem> teamProblemList = teamProblemRepository.findAll();
//        assert teamProblemList.size() == 9
//        for (TeamProblem teamProblem : teamProblemList) {
//            assert teamProblem.getTeam().getContest() != contest2
//        }
    }
}
