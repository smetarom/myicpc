package com.myicpc.service.quest

import com.myicpc.dto.quest.QuestSubmissionDTO
import com.myicpc.enums.ContestParticipantRole
import com.myicpc.model.contest.Contest
import com.myicpc.model.contest.QuestConfiguration
import com.myicpc.model.quest.QuestChallenge
import com.myicpc.model.quest.QuestParticipant
import com.myicpc.model.quest.QuestSubmission
import com.myicpc.model.social.Notification
import com.myicpc.model.teamInfo.ContestParticipant
import com.myicpc.model.teamInfo.ContestParticipantAssociation
import com.myicpc.repository.quest.QuestParticipantRepository
import com.myicpc.repository.quest.QuestSubmissionRepository
import com.myicpc.repository.social.NotificationRepository
import com.myicpc.repository.teamInfo.ContestParticipantAssociationRepository
import org.junit.Assert
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

/**
 * Unit tests for {@link QuestService}
 *
 * @author Roman Smetana
 */
class QuestServiceTest extends GroovyTestCase {
    @Mock
    private QuestParticipantRepository questParticipantRepository;

    @Mock
    private QuestSubmissionRepository questSubmissionRepository;

    @Mock
    private ContestParticipantAssociationRepository contestParticipantAssociationRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private QuestService questService

    @Override
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this)
    }

    void testGetQuestNotifications() {
        def contest = new Contest(id: 1L, questConfiguration: new QuestConfiguration(hashtagPrefix: "Quest"))
        def page = Mockito.mock(Page.class)
        def notifications = [
                new Notification(id: 1L),
                new Notification(id: 2L),
                new Notification(id: 3L)
        ]

        Mockito.when(page.getContent()).thenReturn(notifications)
        Mockito.when(notificationRepository.findByHashtagAndNotificationTypes("%|Quest%", QuestService.QUEST_TIMELINE_TYPES, contest, new PageRequest(0, QuestService.POSTS_PER_PAGE)))
                .thenReturn(page)

        def result = questService.getQuestNotifications(contest)

        Assert.assertEquals notifications, result
    }

    void testGetQuestNotificationsNullContest() {
        def notifications = questService.getQuestNotifications(null)

        Assert.assertTrue notifications.isEmpty()
    }

    void testGetQuestParticipantBySocialUsernamesTwitter() {
        def contest = new Contest(id: 1L)
        def questParticipant = new QuestParticipant(id: 1L)
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, "twitterUsername"))
            .thenReturn(questParticipant)

        def participant = questService.getQuestParticipantBySocialUsernames("twitterUsername", "vineUsername", "instagramUsername", contest)

        Assert.assertEquals questParticipant, participant
    }

    void testGetQuestParticipantBySocialUsernamesNonExistingTwitter() {
        def contest = new Contest(id: 1L)
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, "twitterUsername"))
                .thenReturn(null)

        def participant = questService.getQuestParticipantBySocialUsernames("twitterUsername", "vineUsername", "instagramUsername", contest)

        Assert.assertNull participant
    }

    void testGetQuestParticipantBySocialUsernamesVine() {
        def contest = new Contest(id: 1L)
        def questParticipant = new QuestParticipant(id: 1L)
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantVineUsernameIgnoreCase(contest, "vineUsername"))
                .thenReturn(questParticipant)

        def participant = questService.getQuestParticipantBySocialUsernames("twitterUsername", "vineUsername", "instagramUsername", contest)

        Assert.assertEquals questParticipant, participant
    }

    void testGetQuestParticipantBySocialUsernamesNonExistingVine() {
        def contest = new Contest(id: 1L)
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantVineUsernameIgnoreCase(contest, "vineUsername"))
                .thenReturn(null)

        def participant = questService.getQuestParticipantBySocialUsernames("twitterUsername", "vineUsername", "instagramUsername", contest)

        Assert.assertNull participant
    }

    void testGetQuestParticipantBySocialUsernamesInstagram() {
        def contest = new Contest(id: 1L)
        def questParticipant = new QuestParticipant(id: 1L)
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantInstagramUsernameIgnoreCase(contest, "instagramUsername"))
                .thenReturn(questParticipant)

        def participant = questService.getQuestParticipantBySocialUsernames("twitterUsername", "vineUsername", "instagramUsername", contest)

        Assert.assertEquals questParticipant, participant
    }

    void testGetQuestParticipantBySocialUsernamesNonExistingInstagram() {
        def contest = new Contest(id: 1L)
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantInstagramUsernameIgnoreCase(contest, "instagramUsername"))
                .thenReturn(null)

        def participant = questService.getQuestParticipantBySocialUsernames("twitterUsername", "vineUsername", "instagramUsername", contest)

        Assert.assertNull participant
    }

    void testGetQuestParticipantBySocialUsernamesNullUsernames() {
        def contest = new Contest(id: 1L)
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantInstagramUsernameIgnoreCase(contest, "instagramUsername"))
                .thenReturn(null)

        def participant = questService.getQuestParticipantBySocialUsernames(null, null, null, contest)

        Assert.assertNull participant
    }

    void testGetParticipantsWithRoles() {
        def roles = [ContestParticipantRole.ATTENDEE, ContestParticipantRole.COACH, ContestParticipantRole.CONTESTANT, ContestParticipantRole.RESERVE, ContestParticipantRole.STAFF]
        def contest = new Contest(id: 1L)
        def contestParticipants = [new ContestParticipant(id: 1L)]
        def contestParticipantAssociations = [new ContestParticipantAssociation(contestParticipant: contestParticipants[0], contestParticipantRole: ContestParticipantRole.CONTESTANT)]
        def participants = [new QuestParticipant(id: 1L, contestParticipant: contestParticipants[0])]
        def submissions = [
                new QuestSubmissionDTO(1L, 1L, QuestSubmission.QuestSubmissionState.ACCEPTED, null),
                new QuestSubmissionDTO(1L, 2L, QuestSubmission.QuestSubmissionState.REJECTED, "reason")
        ]

        Mockito.when(questParticipantRepository.findByContest(contest)).thenReturn(participants)
        Mockito.when(contestParticipantAssociationRepository.findByContestParticipantIn(contestParticipants)).thenReturn(contestParticipantAssociations)
        Mockito.when(questSubmissionRepository.findQuestSubmissionDTOByQuestParticipantId([1L], contest)).thenReturn(submissions)

        def resultParticipants = questService.getParticipantsWithRoles(roles, contest)

        Assert.assertEquals 1, resultParticipants.size()
        def resultParticipant = resultParticipants[0]
        Assert.assertEquals 1, resultParticipant.acceptedSubmissions

        Assert.assertEquals 2, resultParticipant.submissionMap.values().size()
        Assert.assertEquals QuestSubmission.QuestSubmissionState.ACCEPTED, resultParticipant.submissionMap[1L].submissionState
        Assert.assertEquals QuestSubmission.QuestSubmissionState.REJECTED, resultParticipant.submissionMap[2L].submissionState

        Assert.assertEquals 1, resultParticipant.contestParticipantRoles.size()
        Assert.assertEquals ContestParticipantRole.CONTESTANT, resultParticipant.contestParticipantRoles[0]

    }


    void testGetParticipantsWithRolesNullContest() {
        Mockito.when(questParticipantRepository.findByRoles([], null, null)).thenReturn([])

        def participants = questService.getParticipantsWithRoles([], null)
        Assert.assertTrue participants.isEmpty()
    }

    void testGetParticipantsWithRolesNullRoles() {
        def contest = new Contest(id: 1L)
        Mockito.when(questParticipantRepository.findByRoles(null, contest, null)).thenReturn([])

        def participants = questService.getParticipantsWithRoles(null, contest)
        Assert.assertTrue participants.isEmpty()
    }

    void testApplyHashtagPrefix() {
        def challenges = [
                new QuestChallenge(id: 1L),
                new QuestChallenge(id: 2L),
                new QuestChallenge(id: 3L)
        ]
        def hashtagPrefix = "Quest"
        QuestService.applyHashtagPrefix(hashtagPrefix, challenges)
        challenges.each {
            Assert.assertEquals hashtagPrefix, it.hashtagPrefix
        }
    }

    void testApplyHashtagPrefixNullChallenges() {
        def hashtagPrefix = "Quest"
        QuestService.applyHashtagPrefix(hashtagPrefix, null)
    }

    void testApplyHashtagPrefixNullHashtagPrefix() {
        def challenges = [
                new QuestChallenge(id: 1L),
                new QuestChallenge(id: 2L),
                new QuestChallenge(id: 3L)
        ]
        QuestService.applyHashtagPrefix(null, challenges)
    }

    void testApplyHashtagPrefixToSubmissions() {
        def challenges = [
                new QuestChallenge(id: 1L),
                new QuestChallenge(id: 2L),
                new QuestChallenge(id: 3L)
        ]
        def submissions = [
                new QuestSubmission(id: 1L, challenge: challenges[0]),
                new QuestSubmission(id: 2L, challenge: challenges[1]),
                new QuestSubmission(id: 3L, challenge: challenges[2])
        ]
        def hashtagPrefix = "Quest"
        QuestService.applyHashtagPrefixToSubmissions(hashtagPrefix, submissions)
        submissions.each {
            Assert.assertEquals hashtagPrefix, it.challenge.hashtagPrefix
        }
    }

    void testApplyHashtagPrefixToSubmissionsNullHashtagPrefix() {
        def challenges = [
                new QuestChallenge(id: 1L),
                new QuestChallenge(id: 2L),
                new QuestChallenge(id: 3L)
        ]
        def submissions = [
                new QuestSubmission(id: 1L, challenge: challenges[0]),
                new QuestSubmission(id: 2L, challenge: challenges[1]),
                new QuestSubmission(id: 3L, challenge: challenges[2])
        ]
        QuestService.applyHashtagPrefixToSubmissions(null, submissions)
    }

    void testApplyHashtagPrefixToSubmissionsNullSubmissions() {
        def hashtagPrefix = "Quest"
        QuestService.applyHashtagPrefixToSubmissions(hashtagPrefix, null)
    }
}
