package com.myicpc.repository.eventFeed

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.myicpc.model.contest.Contest
import com.myicpc.model.eventFeed.EventFeedControl
import com.myicpc.repository.AbstractRepositoryTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional

@Transactional
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/eventFeed/EventFeedControlRepositoryTest.xml"])
@DatabaseTearDown("classpath:dbunit/CleanDatabase.xml")
public class EventFeedControlRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private EventFeedControlRepository eventFeedControlRepository;

    @Test
    public void testFindByContest() {
        Contest contest = contestRepository.findOne(1L);

        EventFeedControl eventFeedControl = eventFeedControlRepository.findByContest(contest);
        assert eventFeedControl != null
        assert eventFeedControl.getId() == 1
        assert eventFeedControl.getContest() == contest
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void unique_contestId() {
        Contest contest = contestRepository.findOne(1L);
        EventFeedControl eventFeedControl = new EventFeedControl(contest: contest);
        eventFeedControlRepository.saveAndFlush(eventFeedControl);
    }

    @Test
    public void testDeleteByContest() {
        Contest contest = contestRepository.findOne(1L);
        Contest contest2 = contestRepository.findOne(2L);

        eventFeedControlRepository.deleteByContest(contest2);
        assert eventFeedControlRepository.findByContest(contest2) == null
        assert eventFeedControlRepository.findByContest(contest) != null
    }
}