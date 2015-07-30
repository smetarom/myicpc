package com.myicpc.repository.quest

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.myicpc.commons.utils.TimeUtils
import com.myicpc.dto.quest.QuestSubmissionFilter
import com.myicpc.enums.SortOrder
import com.myicpc.model.contest.Contest
import com.myicpc.model.quest.QuestChallenge
import com.myicpc.model.quest.QuestParticipant
import com.myicpc.model.quest.QuestSubmission
import com.myicpc.repository.AbstractRepositoryTest
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest

/**
 * DbUnit tests for {@link QuestSubmissionRepository}
 *
 * @author Roman Smetana
 */
@DatabaseSetup(["classpath:dbunit/contest/ContestRepositoryTest.xml", "classpath:dbunit/quest/QuestSubmissionRepositoryTest.xml"])
class QuestSubmissionRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private QuestSubmissionRepository questSubmissionRepository;

    @Test
    void testGetVoteWinnersSubmissions() {
        def contest = new Contest(id: 1L)
        def submissions = questSubmissionRepository.getVoteWinnersSubmissions(contest)
        Assert.assertEquals 2, submissions.size()
        submissions.each {
            Assert.assertEquals 1, it.challenge.contest.id
            Assert.assertEquals QuestSubmission.VoteSubmissionState.VOTE_WINNER, it.voteSubmissionState
            Assert.assertEquals QuestSubmission.QuestSubmissionState.ACCEPTED, it.submissionState
        }
    }

    @Test
    void testGetVoteWinnersSubmissionsNullContest() {
        def submissions = questSubmissionRepository.getVoteWinnersSubmissions(null)
        Assert.assertTrue submissions.isEmpty()
    }

    @Test
    void testGetVoteInProgressSubmissions() {
        def contest = new Contest(id: 1L)
        def submissions = questSubmissionRepository.getVoteInProgressSubmissions(contest)
        Assert.assertEquals 2, submissions.size()
        submissions.each {
            Assert.assertEquals 1, it.challenge.contest.id
            Assert.assertEquals QuestSubmission.VoteSubmissionState.IN_PROGRESS, it.voteSubmissionState
            Assert.assertEquals QuestSubmission.QuestSubmissionState.ACCEPTED, it.submissionState
        }
    }

    @Test
    void testGetVoteInProgressSubmissionsNullContest() {
        def submissions = questSubmissionRepository.getVoteInProgressSubmissions(null)
        Assert.assertTrue submissions.isEmpty()
    }

    @Test
    void testGetVoteEligibleSubmissions() {
        def contest = new Contest(id: 1L)
        def submissions = questSubmissionRepository.getVoteEligibleSubmissions(contest, null)
        Assert.assertEquals 1, submissions.size()
        submissions.each {
            Assert.assertEquals 1, it.challenge.contest.id
            Assert.assertNull it.voteSubmissionState
            Assert.assertEquals QuestSubmission.QuestSubmissionState.ACCEPTED, it.submissionState
        }
    }

    @Test
    void testGetVoteEligibleSubmissionsNullContest() {
        def submissions = questSubmissionRepository.getVoteEligibleSubmissions(null, null)
        Assert.assertTrue submissions.isEmpty()
    }

    @Test
    void testGetMaxNotificationId() {
        def contest = new Contest(id: 1L)
        def maxNotificationId = questSubmissionRepository.getMaxNotificationId(contest)
        Assert.assertEquals 9, maxNotificationId
    }

    @Test
    void testGetMaxNotificationIdNotExistingNotification() {
        def contest = new Contest(id: -1L)
        def maxNotificationId = questSubmissionRepository.getMaxNotificationId(contest)
        Assert.assertEquals null, maxNotificationId
    }

    @Test
    void testGetMaxNotificationIdNullContest() {
        def maxNotificationId = questSubmissionRepository.getMaxNotificationId(null)
        Assert.assertEquals null, maxNotificationId
    }

    @Test
    void testFindQuestSubmissionDTOByQuestParticipantId() {
        def participantsIds = [2L, 3L]
        def contest = new Contest(id: 1L)

        def submissionDTOs = questSubmissionRepository.findQuestSubmissionDTOByQuestParticipantId(participantsIds, contest)
        Assert.assertEquals 6, submissionDTOs.size()
    }

    @Test
    void testGetFilteredQuestSubmissions() {
        def contest = new Contest(id: 1L)
        def challenge = new QuestChallenge(id: 1L)
        def questParticipant = new QuestParticipant(id: 1L)
        def filter = new QuestSubmissionFilter(challenge: challenge, participant: questParticipant, submissionState: QuestSubmission.QuestSubmissionState.ACCEPTED)
        def pageable = new PageRequest(0, 1)

        def submissionPage = questSubmissionRepository.getFilteredQuestSubmissions(filter, contest, pageable)
        Assert.assertEquals 1, submissionPage.getContent().size()
        Assert.assertEquals 1, submissionPage.getTotalElements()
        submissionPage.getContent().each {
            Assert.assertEquals 1L, it.challenge.contest.id
            Assert.assertEquals challenge.id, it.challenge.id
            Assert.assertEquals questParticipant.id, it.participant.id
            Assert.assertEquals QuestSubmission.QuestSubmissionState.ACCEPTED, it.submissionState
        }
    }

    @Test
    void testGetFilteredQuestSubmissionsFilteredBySubmissionState() {
        def contest = new Contest(id: 1L)
        def filter = new QuestSubmissionFilter(submissionState: QuestSubmission.QuestSubmissionState.ACCEPTED)
        def pageable = new PageRequest(0, 2)

        def submissionPage = questSubmissionRepository.getFilteredQuestSubmissions(filter, contest, pageable)
        Assert.assertEquals 2, submissionPage.getContent().size()
        Assert.assertEquals 5, submissionPage.getTotalElements()
        submissionPage.getContent().each {
            Assert.assertEquals 1L, it.challenge.contest.id
            Assert.assertEquals QuestSubmission.QuestSubmissionState.ACCEPTED, it.submissionState
        }
    }

    @Test
    void testGetFilteredQuestSubmissionsFilteredByQuestChallenge() {
        def contest = new Contest(id: 1L)
        def challenge = new QuestChallenge(id: 1L)
        def filter = new QuestSubmissionFilter(challenge: challenge)
        def pageable = new PageRequest(0, 2)

        def submissionPage = questSubmissionRepository.getFilteredQuestSubmissions(filter, contest, pageable)
        Assert.assertEquals 2, submissionPage.getContent().size()
        Assert.assertEquals 3, submissionPage.getTotalElements()
        submissionPage.getContent().each {
            Assert.assertEquals 1L, it.challenge.contest.id
            Assert.assertEquals challenge.id, it.challenge.id
        }
    }

    @Test
    void testGetFilteredQuestSubmissionsFilteredByQuestParticipant() {
        def contest = new Contest(id: 1L)
        def questParticipant = new QuestParticipant(id: 1L)
        def filter = new QuestSubmissionFilter(participant: questParticipant)
        def pageable = new PageRequest(0, 2)

        def submissionPage = questSubmissionRepository.getFilteredQuestSubmissions(filter, contest, pageable)
        Assert.assertEquals 2, submissionPage.getContent().size()
        Assert.assertEquals 3, submissionPage.getTotalElements()
        submissionPage.getContent().each {
            Assert.assertEquals 1L, it.challenge.contest.id
            Assert.assertEquals questParticipant.id, it.participant.id
        }
    }

    @Test
    void testGetFilteredQuestSubmissionsSortedTimestampDesc() {
        def filter = new QuestSubmissionFilter(sortOrder: SortOrder.DESC)
        def contest = new Contest(id: 1L)
        def pageable = new PageRequest(0, 5)

        def submissionPage = questSubmissionRepository.getFilteredQuestSubmissions(filter, contest, pageable)
        Assert.assertEquals 5, submissionPage.getContent().size()
        Assert.assertEquals 9, submissionPage.getTotalElements()
        def tmpDate = TimeUtils.getDate(2100, 1, 1)
        submissionPage.getContent().each {
            if (it.notification.timestamp != null) {
                Assert.assertFalse tmpDate.before(it.notification.timestamp)
                tmpDate = it.notification.timestamp
            }
        }
    }
    @Test
    void testGetFilteredQuestSubmissionsSortedTimestampAsc() {
        def filter = new QuestSubmissionFilter(sortOrder: SortOrder.ASC)
        def contest = new Contest(id: 1L)
        def pageable = new PageRequest(0, 5)

        def submissionPage = questSubmissionRepository.getFilteredQuestSubmissions(filter, contest, pageable)
        Assert.assertEquals 5, submissionPage.getContent().size()
        Assert.assertEquals 9, submissionPage.getTotalElements()
        def tmpDate = TimeUtils.getDate(1990, 1, 1)
        submissionPage.getContent().each {
            if (it.notification.timestamp != null) {
                Assert.assertFalse tmpDate.after(it.notification.timestamp)
                tmpDate = it.notification.timestamp
            }
        }
    }

    @Test
    void testGetFilteredQuestSubmissionsEmptyFilter() {
        def contest = new Contest(id: 1L)
        def filter = new QuestSubmissionFilter()
        def pageable = new PageRequest(0, 5)

        def submissionPage = questSubmissionRepository.getFilteredQuestSubmissions(filter, contest, pageable)
        Assert.assertEquals 5, submissionPage.getContent().size()
        Assert.assertEquals 9, submissionPage.getTotalElements()
        submissionPage.getContent().each {
            Assert.assertEquals 1L, it.challenge.contest.id
        }
    }

    @Test
    void testGetFilteredQuestSubmissionsNullFilter() {
        def contest = new Contest(id: 1L)
        def pageable = new PageRequest(0, 5)

        def submissionPage = questSubmissionRepository.getFilteredQuestSubmissions(null, contest, pageable)
        Assert.assertTrue submissionPage.getContent().isEmpty()
        Assert.assertEquals 0, submissionPage.getTotalElements()
    }

    @Test
    void testGetFilteredQuestSubmissionsNullContest() {
        def filter = new QuestSubmissionFilter()
        def pageable = new PageRequest(0, 5)

        def submissionPage = questSubmissionRepository.getFilteredQuestSubmissions(filter, null, pageable)
        Assert.assertTrue submissionPage.getContent().isEmpty()
        Assert.assertEquals 0, submissionPage.getTotalElements()
    }

    @Test
    void testGetFilteredQuestSubmissionsNullPageable() {
        def filter = new QuestSubmissionFilter()
        def contest = new Contest(id: 1L)

        def submissionPage = questSubmissionRepository.getFilteredQuestSubmissions(filter, contest, null)
        Assert.assertEquals 9, submissionPage.getContent().size()
        Assert.assertEquals 9, submissionPage.getTotalElements()
    }
}
