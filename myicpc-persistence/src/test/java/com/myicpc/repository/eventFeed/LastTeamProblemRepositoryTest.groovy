package com.myicpc.repository.eventFeed

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.myicpc.model.contest.Contest
import com.myicpc.model.eventFeed.LastTeamProblem
import com.myicpc.model.eventFeed.Problem
import com.myicpc.model.eventFeed.Team
import com.myicpc.model.eventFeed.TeamProblem
import com.myicpc.repository.AbstractRepositoryTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional

/**
 * @author Roman Smetana
 */
@Transactional
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/eventFeed/LastTeamProblemRepositoryTest.xml"])
@DatabaseTearDown("classpath:dbunit/CleanDatabase.xml")
class LastTeamProblemRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private LastTeamProblemRepository lastTeamProblemRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamProblemRepository teamProblemRepository;

    @Test
    void testFindByTeamAndProblem() {
        Team team = teamRepository.findOne(1L);
        Problem problem = problemRepository.findOne(2L);
        LastTeamProblem lastTeamProblem = lastTeamProblemRepository.findByTeamAndProblem(team, problem);
        assert lastTeamProblem != null
        assert lastTeamProblem.getId() == 4
    }

    @Test
    void testFindByTeam() {
        Team team = teamRepository.findOne(2L);
        List<LastTeamProblem> lastTeamProblemList = lastTeamProblemRepository.findByTeam(team);
        assert lastTeamProblemList.size() == 2
    }

    @Test(expected = DataIntegrityViolationException.class)
    void unique_teamAndProblemAndTeamProblem() {
        Team team = teamRepository.findOne(2L);
        Problem problem = problemRepository.findOne(3L);
        TeamProblem teamProblem = teamProblemRepository.findOne(6L);
        LastTeamProblem lastTeamProblem = new LastTeamProblem(team: team, problem: problem, teamProblem: teamProblem);

        lastTeamProblemRepository.saveAndFlush(lastTeamProblem);
    }

    @Test
    void testDeleteByContest() {
        Contest contest2 = contestRepository.findOne(2L);

        lastTeamProblemRepository.deleteByContest(contest2);

        List<LastTeamProblem> lastTeamProblemList = lastTeamProblemRepository.findAll();
        for (LastTeamProblem lastTeamProblem : lastTeamProblemList) {
            assert lastTeamProblem.getTeam().getContest() != contest2
        }
        assert lastTeamProblemList.size() == 7
    }
}
