package com.myicpc.repository.eventFeed

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.myicpc.model.contest.Contest
import com.myicpc.model.eventFeed.Judgement
import com.myicpc.repository.AbstractRepositoryTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional

/**
 * @author Roman Smetana
 */
@Transactional
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/eventFeed/JudgementRepositoryTest.xml"])
@DatabaseTearDown("classpath:dbunit/CleanDatabase.xml")
class JudgementRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private JudgementRepository judgementRepository;

    @Test
    void findByCodeAndContest() {
        Contest contest = contestRepository.findOne(1L);
        Judgement judgement = judgementRepository.findByCodeAndContest("AC", contest);
        assert judgement != null
        assert judgement.getId() == 3
    }

    @Test
    void findByContest() {
        Contest contest = contestRepository.findOne(1L);
        List<Judgement> judgementList = judgementRepository.findByContest(contest);
        assert judgementList.size() == 3
    }

    @Test(expected = DataIntegrityViolationException.class)
    void unique_codeAndContest() {
        Contest contest = contestRepository.findOne(1L);
        Judgement judgement = new Judgement(code: "AC", contest: contest);
        judgementRepository.saveAndFlush(judgement);
    }

    @Test
    void testGetJudgmentReport() {
        // TODO implement after refactoring insight
    }

    @Test
    void testDeleteByContest() {
        Contest contest = contestRepository.findOne(1L);
        Contest contest2 = contestRepository.findOne(2L);

        judgementRepository.deleteByContest(contest);

        assert judgementRepository.findByContest(contest).isEmpty()
        assert judgementRepository.findByContest(contest2).size() == 2
    }
}
