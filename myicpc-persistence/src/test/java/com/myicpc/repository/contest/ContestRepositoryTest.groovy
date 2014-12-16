package com.myicpc.repository.contest;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.AbstractRepositoryTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Roman Smetana
 */
@DatabaseSetup("classpath:dbunit/contest/ContestRepositoryTest.xml")
class ContestRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private ContestRepository contestRepository;

    @Test
    public void findByName() {
        Contest contest = contestRepository.findByName("CTU Open");
        assert contest != null
        assert contest.getName() == "CTU Open"
    }

    @Test
    public void findByName_NonExisting() {
        Contest contest = contestRepository.findByName("NonExisting Name");
        assert contest == null
    }

    @Test
    public void findByCode() {
        Contest contest = contestRepository.findByCode("CTU-Open-2013");
        assert contest != null
        assert contest.getCode() == "CTU-Open-2013"
    }

    @Test
    public void findByCode_NonExisting() {
        Contest contest = contestRepository.findByCode("NonExisting Code");
        assert contest == null
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void create_DuplicatedName() {
        Contest contest = new Contest(name: "CTU Open");
        contestRepository.save(contest);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void create_DuplicatedShortName() {
        Contest contest = new Contest(shortName: "CTU");
        contestRepository.save(contest);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void create_DuplicatedCode() {
        Contest contest = new Contest(code: "CTU-Open-2013");
        contestRepository.save(contest);
    }

    @Test
    @Transactional
    public void delete() {
        contestRepository.delete(3L);
        assert contestRepository.findOne(3L) == null
    }
}
