package com.myicpc.repository.poll

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.myicpc.commons.utils.TimeUtils
import com.myicpc.model.contest.Contest
import com.myicpc.repository.AbstractRepositoryTest
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * DbUnit tests for {@link PollRepository}
 *
 * @author Roman Smetana
 */
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/poll/PollRepositoryTest.xml"])
class PollRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private PollRepository pollRepository;

    @Test
    void testFindByContestOrderByStartDateDesc() {
        def contest = new Contest(id: 1L)
        def polls = pollRepository.findByContestOrderByStartDateDesc(contest)

        Assert.assertEquals 3, polls.size()
        Assert.assertEquals 3, polls[0].id
        Assert.assertEquals 2, polls[1].id
        Assert.assertEquals 1, polls[2].id
    }

    @Test
    void testFindByContestOrderByStartDateDescNullContest() {
        def polls = pollRepository.findByContestOrderByStartDateDesc(null)
        Assert.assertTrue polls.isEmpty()
    }

    @Test
    void testFindOpenPolls() {
        def date = TimeUtils.getDate(2014, 12, 16)
        def contest = new Contest(id: 1L)

        def polls = pollRepository.findOpenPolls(contest, date)

        Assert.assertEquals 2, polls.size()
        Assert.assertEquals 2, polls[0].id
        Assert.assertEquals 1, polls[1].id
    }

    @Test
    void testFindOpenPollsNullContest() {
        def polls = pollRepository.findOpenPolls(null, new Date())
        Assert.assertTrue polls.isEmpty()
    }

    @Test
    void testFindOpenPollsNullDate() {
        def polls = pollRepository.findOpenPolls(new Contest(id: 1L), null)
        Assert.assertTrue polls.isEmpty()
    }

    @Test
    void testFindAllNonpublishedStartedPolls() {
        def date = TimeUtils.getDate(2014, 12, 16)
        def contest = new Contest(id: 2L)
        def polls = pollRepository.findAllNonpublishedStartedPolls(date, contest)

        Assert.assertEquals 1, polls.size()
        Assert.assertEquals 4, polls[0].id
    }

    @Test
    void testFindAllNonpublishedStartedPollsNullDate() {
        def contest = new Contest(id: 2L)
        def polls = pollRepository.findAllNonpublishedStartedPolls(null, contest)

        Assert.assertTrue polls.isEmpty()
    }

    @Test
    void testFindAllNonpublishedStartedPollsNullContest() {
        def polls = pollRepository.findAllNonpublishedStartedPolls(new Date(), null)

        Assert.assertTrue polls.isEmpty()
    }
}
