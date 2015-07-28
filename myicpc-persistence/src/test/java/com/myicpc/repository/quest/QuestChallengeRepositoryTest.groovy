package com.myicpc.repository.quest

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.myicpc.commons.utils.TimeUtils
import com.myicpc.model.contest.Contest
import com.myicpc.model.quest.QuestParticipant
import com.myicpc.repository.AbstractRepositoryTest
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * DbUnit tests for {@link QuestChallengeRepository}
 *
 * @author Roman Smetana
 */
@DatabaseSetup("classpath:dbunit/quest/QuestChallengeRepositoryTest.xml")
class QuestChallengeRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private QuestChallengeRepository questChallengeRepository;

    @Test
    void testFindByContestAvailableChallenges() {
        def date = TimeUtils.getDate(2014, 6, 8)
        def challenges = questChallengeRepository.findByContestAvailableChallenges(date, new Contest(id: 1L));

        Assert.assertEquals 2, challenges.size()
        challenges.each {
            Assert.assertEquals 1L, it.contest.id
            Assert.assertFalse date.before(it.startDate)
            Assert.assertTrue it.endDate == null || !date.after(it.endDate)
        }
    }

    @Test
    void testFindByContestAvailableChallengesNullContest() {
        def date = TimeUtils.getDate(2014, 6, 8)
        def challenges = questChallengeRepository.findByContestAvailableChallenges(date, null);
        Assert.assertTrue challenges.isEmpty()
    }

    @Test
    void testFindByContestAvailableChallengesNullDate() {
        def challenges = questChallengeRepository.findByContestAvailableChallenges(null, new Contest(id: 1L));
        Assert.assertTrue challenges.isEmpty()
    }

    @Test
    void testFindOpenChallengesByContestOrderByName() {
        def date = TimeUtils.getDate(2014, 6, 9)
        def challenges = questChallengeRepository.findOpenChallengesByContestOrderByName(date, new Contest(id: 1L));

        Assert.assertEquals 3, challenges.size()
        Assert.assertEquals "Quest 1", challenges[0].name
        Assert.assertEquals "Quest 2", challenges[1].name
        Assert.assertEquals "Quest 3", challenges[2].name
    }

    @Test
    void testFindOpenChallengesByContestOrderByNameNullContest() {
        def date = TimeUtils.getDate(2014, 6, 8)
        def challenges = questChallengeRepository.findOpenChallengesByContestOrderByName(date, null);
        Assert.assertTrue challenges.isEmpty()
    }

    @Test
    void testFindOpenChallengesByContestOrderByNameNullDate() {
        def challenges = questChallengeRepository.findOpenChallengesByContestOrderByName(null, new Contest(id: 1L));
        Assert.assertTrue challenges.isEmpty()
    }

    @Test
    void testFindOpenChallengesByContestOrderByHashtag() {
        def date = TimeUtils.getDate(2014, 6, 9)
        def challenges = questChallengeRepository.findOpenChallengesByContestOrderByName(date, new Contest(id: 1L));

        Assert.assertEquals 3, challenges.size()
        Assert.assertEquals "1", challenges[0].hashtagSuffix
        Assert.assertEquals "2", challenges[1].hashtagSuffix
        Assert.assertEquals "3", challenges[2].hashtagSuffix
    }

    @Test
    void testFindOpenChallengesByContestOrderByHashtagNullContest() {
        def date = TimeUtils.getDate(2014, 6, 8)
        def challenges = questChallengeRepository.findOpenChallengesByContestOrderByHashtag(date, null);
        Assert.assertTrue challenges.isEmpty()
    }

    @Test
    void testFindOpenChallengesByContestOrderByHashtagNullDate() {
        def challenges = questChallengeRepository.findOpenChallengesByContestOrderByHashtag(null, new Contest(id: 1L));
        Assert.assertTrue challenges.isEmpty()
    }

    @Test
    void testFindAvailableChallengesByQuestParticipantOrderByName() {
        def questParticipant = new QuestParticipant(id: 1L)
        def contest = new Contest(id: 1L)
        def date = TimeUtils.getDate(2014, 6, 9)

        def challenges = questChallengeRepository.findAvailableChallengesByQuestParticipantOrderByName(date, questParticipant, contest)
        Assert.assertEquals 1, challenges.size()
        challenges.each {
            Assert.assertEquals 1, it.contest.id
            Assert.assertFalse date.before(it.startDate)
            // challenges with ID 1 or 2 are solved by quest participant
            Assert.assertTrue it.id != 1
            Assert.assertTrue it.id != 2
        }
    }

    @Test
    void testFindAvailableChallengesByQuestParticipantOrderByNameNullContest() {
        def date = TimeUtils.getDate(2014, 6, 8)
        def questParticipant = new QuestParticipant(id: 1L)
        def challenges = questChallengeRepository.findAvailableChallengesByQuestParticipantOrderByName(date, questParticipant, null);
        Assert.assertTrue challenges.isEmpty()
    }

    @Test
    void testFindAvailableChallengesByQuestParticipantOrderByNameNullQuestParticipant() {
        def date = TimeUtils.getDate(2014, 6, 8)
        def challenges = questChallengeRepository.findAvailableChallengesByQuestParticipantOrderByName(date, null, new Contest(id: 1L));
        Assert.assertEquals 2, challenges.size()
        challenges.each {
            Assert.assertEquals 1, it.contest.id
            Assert.assertFalse date.before(it.startDate)
        }
    }

    @Test
    void testFindAvailableChallengesByQuestParticipantOrderByNameNullDate() {
        def questParticipant = new QuestParticipant(id: 1L)
        def challenges = questChallengeRepository.findAvailableChallengesByQuestParticipantOrderByName(null, questParticipant, new Contest(id: 1L));
        Assert.assertTrue challenges.isEmpty()
    }

    @Test
    void testFindAllNonpublishedStartedChallenges() {
        def date = TimeUtils.getDate(2014, 6, 9)
        def contest = new Contest(id: 2L)
        def challenges = questChallengeRepository.findAllNonpublishedStartedChallenges(date, contest)

        Assert.assertEquals 1, challenges.size()
        challenges.each {
            Assert.assertEquals 2, it.contest.id
            Assert.assertFalse date.before(it.startDate)
            Assert.assertFalse it.published
        }

    }

    @Test
    void testFindAllNonpublishedStartedChallengesNullContest() {
        def date = TimeUtils.getDate(2014, 6, 8)
        def challenges = questChallengeRepository.findAllNonpublishedStartedChallenges(date, null);
        Assert.assertTrue challenges.isEmpty()
    }

    @Test
    void testFindAllNonpublishedStartedChallengesNullDate() {
        def challenges = questChallengeRepository.findAllNonpublishedStartedChallenges(null, new Contest(id: 2L));
        Assert.assertTrue challenges.isEmpty()
    }
}
