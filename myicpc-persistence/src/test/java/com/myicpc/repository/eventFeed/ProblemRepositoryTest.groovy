package com.myicpc.repository.eventFeed

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.myicpc.model.contest.Contest
import com.myicpc.model.eventFeed.Problem
import com.myicpc.repository.AbstractRepositoryTest
import com.myicpc.repository.contest.ContestRepository
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/eventFeed/ProblemRepositoryTest.xml"])
@DatabaseTearDown("classpath:dbunit/CleanDatabase.xml")
public class ProblemRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ProblemRepository problemRepository;

    @Test
    public void testFindByCodeAndContest() {
        Contest contest = contestRepository.findOne(1L);

        Problem problem = problemRepository.findByCodeAndContest("A", contest);
        assert  problem != null
        assert problem.getCode() == "A"
        assert problem.getName() == "Problem 1"
    }

    @Test
    public void testFindBySystemIdAndContest() {
        Contest contest = contestRepository.findOne(1L);

        Problem problem = problemRepository.findBySystemIdAndContest(2L, contest);
        assert problem != null
        assert problem.getSystemId() == 2
        assert problem.getName() == "Problem 2"
    }

    @Test
    public void testFindByContestOrderByCodeAsc() {
        Contest contest = contestRepository.findOne(1L);

        List<Problem> problems = problemRepository.findByContestOrderByCodeAsc(contest);

        assert problems.size() == 3
        assert problems[0].getCode() == "A"
        assert problems[1].getCode() == "B"
        assert problems[2].getCode() == "C"
    }

    @Test
    public void testCountByContest() {
        Contest contest = contestRepository.findOne(1L);
        assert problemRepository.countByContest(contest) == 3L
    }

    @Test
    public void testGetProblemReport() {
        // TODO implement after refactoring insight
    }

    @Test
    public void testDeleteByContest() {
        Contest contest = contestRepository.findOne(1L);
        Contest contest2 = contestRepository.findOne(2L);

        problemRepository.deleteByContest(contest);

        assert problemRepository.findByContestOrderByCodeAsc(contest).isEmpty()
        assert problemRepository.findByContestOrderByCodeAsc(contest2).size() == 3
    }
}