package com.myicpc.repository.eventFeed;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.repository.AbstractRepositoryTest;
import com.myicpc.repository.contest.ContestRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@Transactional
@DatabaseSetup({"classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/eventFeed/ProblemRepositoryTest.xml"})
@DatabaseTearDown("classpath:dbunit/CleanDatabase.xml")
public class ProblemRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Test
    public void testFindByCodeAndContest() throws Exception {
        Contest contest = contestRepository.findOne(1L);

        Problem problem = problemRepository.findByCodeAndContest("A", contest);
        Assert.assertNotNull(problem);
        Assert.assertEquals("A", problem.getCode());
        Assert.assertEquals("Problem 1", problem.getName());
    }

    @Test
    public void testFindBySystemIdAndContest() throws Exception {
        Contest contest = contestRepository.findOne(1L);

        Problem problem = problemRepository.findBySystemIdAndContest(2L, contest);
        Assert.assertNotNull(problem);
        Assert.assertEquals(2L, (long) problem.getSystemId());
        Assert.assertEquals("Problem 2", problem.getName());
    }

    @Test
    public void testFindByContestOrderByCodeAsc() throws Exception {
        Contest contest = contestRepository.findOne(1L);

        List<Problem> problems = problemRepository.findByContestOrderByCodeAsc(contest);
        Assert.assertEquals(3, problems.size());
        Assert.assertEquals("A", problems.get(0).getCode());
        Assert.assertEquals("B", problems.get(1).getCode());
        Assert.assertEquals("C", problems.get(2).getCode());
    }

    @Test
    public void testCountByContest() throws Exception {
        Contest contest = contestRepository.findOne(1L);

        Assert.assertEquals(3L, (long) problemRepository.countByContest(contest));
    }

    @Test
    public void testGetProblemReport() throws Exception {
        // TODO implement after refactoring insight
    }

    @Test
    public void testDeleteByContest() throws Exception {
        Contest contest = contestRepository.findOne(1L);
        Contest contest2 = contestRepository.findOne(2L);

        problemRepository.deleteByContest(contest);

        Assert.assertTrue(problemRepository.findByContestOrderByCodeAsc(contest).isEmpty());
        Assert.assertEquals(3, problemRepository.findByContestOrderByCodeAsc(contest2).size());
    }
}