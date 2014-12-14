package com.myicpc.repository.contest;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.myicpc.model.contest.Contest;
import com.myicpc.repository.config.TestPersistenceConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * @author Roman Smetana
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:dbunit/ContestRepositoryTest.xml")
public class ContestRepositoryTest {
    @Autowired
    private ContestRepository contestRepository;

    @Test
    public void findByName() throws Exception {
        Contest contest = contestRepository.findByName("CTU Open");
        Assert.assertNotNull(contest);
        Assert.assertEquals("CTU Open", contest.getName());
    }

    @Test
    public void findByName_NonExisting() throws Exception {
        Contest contest = contestRepository.findByName("NonExisting Name");
        Assert.assertNull(contest);
    }

    @Test
    public void findByCode() throws Exception {
        Contest contest = contestRepository.findByCode("CTU-Open-2013");
        Assert.assertNotNull(contest);
        Assert.assertEquals("CTU-Open-2013", contest.getCode());
    }

    @Test
    public void findByCode_NonExisting() throws Exception {
        Contest contest = contestRepository.findByCode("NonExisting Code");
        Assert.assertNull(contest);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void create_DuplicatedName() throws Exception {
        Contest contest = new Contest();
        contest.setName("CTU Open");
        contestRepository.save(contest);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void create_DuplicatedShortName() throws Exception {
        Contest contest = new Contest();
        contest.setShortName("CTU");
        contestRepository.save(contest);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void create_DuplicatedCode() throws Exception {
        Contest contest = new Contest();
        contest.setCode("CTU-Open-2013");
        contestRepository.save(contest);
    }
}
