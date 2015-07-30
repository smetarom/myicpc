package com.myicpc.service.quest

import com.myicpc.commons.utils.TimeUtils
import com.myicpc.enums.NotificationType
import com.myicpc.model.contest.Contest
import com.myicpc.model.contest.QuestConfiguration
import com.myicpc.model.quest.QuestChallenge
import com.myicpc.model.quest.QuestParticipant
import com.myicpc.model.quest.QuestSubmission
import com.myicpc.model.social.Notification
import com.myicpc.model.teamInfo.ContestParticipant
import com.myicpc.repository.quest.QuestChallengeRepository
import com.myicpc.repository.quest.QuestParticipantRepository
import com.myicpc.repository.quest.QuestSubmissionRepository
import com.myicpc.repository.social.NotificationRepository
import com.myicpc.repository.teamInfo.ContestParticipantRepository
import com.myicpc.service.publish.PublishService
import org.junit.Assert
import org.mockito.*
import org.springframework.data.domain.PageRequest

/**
 * Unit test for {@link QuestSubmissionService}
 * @author Roman Smetana
 */
class QuestSubmissionServiceTest extends GroovyTestCase {

    @Mock
    private QuestChallengeRepository challengeRepository;

    @Mock
    private QuestParticipantRepository questParticipantRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private QuestSubmissionRepository submissionRepository;

    @Mock
    private ContestParticipantRepository contestParticipantRepository;

    @Mock
    private PublishService publishService;

    @InjectMocks
    private QuestSubmissionService questSubmissionService;

    private Contest contest
    private QuestChallenge challenge
    private Notification notification
    private QuestParticipant questParticipant
    private QuestSubmission questSubmission
    private ContestParticipant contestParticipant

    @Override
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this)

        contest = new Contest(id: 1L, hashtag: "C1", questConfiguration: new QuestConfiguration(hashtagPrefix: "Quest"))
        challenge = new QuestChallenge(id: 1L,
                endDate: TimeUtils.getDate(2014, 6, 16),
                defaultPoints: 10,
                hashtagSuffix: "2",
                contest: contest)
        notification = new Notification(id: 1L,
                authorUsername: "username",
                timestamp: TimeUtils.getDate(2014, 6, 15),
                notificationType: NotificationType.TWITTER)
        questParticipant = new QuestParticipant(id: 1L)
        contestParticipant = new ContestParticipant(id: 1L)
        questSubmission = new QuestSubmission(id: 1L)
    }

    void testProcessAllSubmissionsTwitter() {
        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, "username"))
                .thenReturn(questParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(challenge, questParticipant))
                .thenReturn(null)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsTwitterNonExistingQuestParticipant() {
        notification.notificationType = NotificationType.TWITTER

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantInstagramUsernameIgnoreCase(contest, "username"))
                .thenReturn(null)
        Mockito.when(questParticipantRepository.save(Matchers.any(QuestParticipant.class))).then(AdditionalAnswers.returnsFirstArg())
        Mockito.when(contestParticipantRepository.findByTwitterUsernameIgnoreCase("username"))
                .thenReturn(contestParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(Matchers.any(QuestChallenge.class), Matchers.any(QuestParticipant.class)))
                .thenReturn(null)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsInstagram() {
        notification.notificationType = NotificationType.INSTAGRAM

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantInstagramUsernameIgnoreCase(contest, "username"))
                .thenReturn(questParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(challenge, questParticipant))
                .thenReturn(null)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsInstagramNonExistingQuestParticipant() {
        notification.notificationType = NotificationType.INSTAGRAM

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, "username"))
                .thenReturn(null)
        Mockito.when(questParticipantRepository.save(Matchers.any(QuestParticipant.class))).then(AdditionalAnswers.returnsFirstArg())
        Mockito.when(contestParticipantRepository.findByInstagramUsernameIgnoreCase("username"))
                .thenReturn(contestParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(Matchers.any(QuestChallenge.class), Matchers.any(QuestParticipant.class)))
                .thenReturn(null)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsVine() {
        notification.notificationType = NotificationType.VINE

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantVineUsernameIgnoreCase(contest, "username"))
                .thenReturn(questParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(challenge, questParticipant))
                .thenReturn(null)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsVineNonExistingQuestParticipant() {
        notification.notificationType = NotificationType.VINE

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantVineUsernameIgnoreCase(contest, "username"))
                .thenReturn(null)
        Mockito.when(questParticipantRepository.save(Matchers.any(QuestParticipant.class))).then(AdditionalAnswers.returnsFirstArg())
        Mockito.when(contestParticipantRepository.findByVineUsernameIgnoreCase("username"))
                .thenReturn(contestParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(Matchers.any(QuestChallenge.class), Matchers.any(QuestParticipant.class)))
                .thenReturn(null)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsNonExistingContestParticipant() {
        notification.notificationType = NotificationType.ADMIN_NOTIFICATION

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, "username"))
                .thenReturn(null)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(0)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsSkipAcceptedSubmission() {
        questSubmission.submissionState = QuestSubmission.QuestSubmissionState.ACCEPTED

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, "username"))
                .thenReturn(questParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(challenge, questParticipant))
                .thenReturn(questSubmission)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(0)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsPostedAfterDeadline() {
        notification.timestamp = TimeUtils.getDate(2014, 6, 17)

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification])

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(0)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsPostedRetweeted() {
        notification.parentId = 2L

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification])

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(0)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessRecentSubmissions() {
        Mockito.when(submissionRepository.getMaxNotificationId(contest)).thenReturn(1L)
        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContestSinceId("%|Quest2|%", "%|C1|%", 1L, contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, "username"))
                .thenReturn(questParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(challenge, questParticipant))
                .thenReturn(null)

        questSubmissionService.processRecentSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessRecentSubmissionsPostedAfterDeadline() {
        notification.timestamp = TimeUtils.getDate(2014, 6, 17)

        Mockito.when(submissionRepository.getMaxNotificationId(contest)).thenReturn(1L)
        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContestSinceId("%|Quest2|%", "%|C1|%", 1L, contest))
                .thenReturn([notification])

        questSubmissionService.processRecentSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(0)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessRecentSubmissionsRetweetedPost() {
        notification.parentId = 2L

        Mockito.when(submissionRepository.getMaxNotificationId(contest)).thenReturn(1L)
        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContestSinceId("%|Quest2|%", "%|C1|%", 1L, contest))
                .thenReturn([notification])

        questSubmissionService.processRecentSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(0)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessRecentSubmissionsNonExistingContestParticipant() {
        notification.notificationType = NotificationType.ADMIN_NOTIFICATION

        Mockito.when(submissionRepository.getMaxNotificationId(contest)).thenReturn(1L)
        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContestSinceId("%|Quest2|%", "%|C1|%", 1L, contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, "username"))
                .thenReturn(null)

        questSubmissionService.processRecentSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(0)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessRecentSubmissionsSkipAcceptedSubmission() {
        questSubmission.submissionState = QuestSubmission.QuestSubmissionState.ACCEPTED

        Mockito.when(submissionRepository.getMaxNotificationId(contest)).thenReturn(1L)
        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge])
        Mockito.when(notificationRepository.findByHashTagsAndContestSinceId("%|Quest2|%", "%|C1|%", 1L, contest))
                .thenReturn([notification])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, "username"))
                .thenReturn(questParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(challenge, questParticipant))
                .thenReturn(questSubmission)

        questSubmissionService.processRecentSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(0)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testMoveVotingToNextRound() {
        def contest = new Contest(id: 1L, questConfiguration: new QuestConfiguration(deadline: TimeUtils.getDate(2014, 6, 22)))
        def questParticipant = new QuestParticipant(id: 1L)
        def questSubmission1 = new QuestSubmission(id: 1L, votes: 4)
        def questSubmission2 = new QuestSubmission(id: 1L, votes: 3)
        def questSubmission3 = new QuestSubmission(id: 1L, votes: 20, participant: questParticipant)
        def questSubmission4 = new QuestSubmission(id: 1L, votes: 12)

        Mockito.when(submissionRepository.getVoteInProgressSubmissions(contest))
                .thenReturn([questSubmission1, questSubmission2, questSubmission3, questSubmission4])

        def winningSubmission = questSubmissionService.moveVotingToNextRound(contest)
        Assert.assertEquals winningSubmission, questSubmission3
    }

    void testMoveVotingToNextRoundNotEnoughVotePoints() {
        def contest = new Contest(id: 1L, questConfiguration: new QuestConfiguration(deadline: TimeUtils.getDate(2014, 6, 22)))
        def questSubmission1 = new QuestSubmission(id: 1L, votes: 4)
        def questSubmission2 = new QuestSubmission(id: 1L, votes: 3)
        def questSubmission3 = new QuestSubmission(id: 1L, votes: 2)
        def questSubmission4 = new QuestSubmission(id: 1L, votes: 1)

        Mockito.when(submissionRepository.getVoteInProgressSubmissions(contest))
                .thenReturn([questSubmission1, questSubmission2, questSubmission3, questSubmission4])

        def winningSubmission = questSubmissionService.moveVotingToNextRound(contest)
        Assert.assertNull winningSubmission
    }

    void testMoveVotingToNextRoundGenerateNextRound() {
        def contest = new Contest(id: 1L, questConfiguration: new QuestConfiguration(deadline: TimeUtils.getDate(2114, 6, 22)))
        def questSubmission1 = new QuestSubmission(id: 1L, votes: 4)
        def questSubmission2 = new QuestSubmission(id: 1L, votes: 3)
        def questSubmission3 = new QuestSubmission(id: 1L, votes: 2)
        def questSubmission4 = new QuestSubmission(id: 1L, votes: 1)

        Mockito.when(submissionRepository.getVoteInProgressSubmissions(contest))
                .thenReturn([])
        Mockito.when(submissionRepository.getVoteEligibleSubmissions(contest, new PageRequest(0, QuestSubmissionService.VOTE_ROUND_SIZE)))
                .thenReturn([questSubmission1, questSubmission2, questSubmission3, questSubmission4])

        def winningSubmission = questSubmissionService.moveVotingToNextRound(contest)
        Assert.assertNull winningSubmission
    }
}
