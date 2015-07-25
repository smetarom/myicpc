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
import org.mockito.*

/**
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

    @Override
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this)
    }

    void testProcessAllSubmissionsTwitter() {
        def contest = new Contest(id: 1L, hashtag: "C1", questConfiguration: new QuestConfiguration(hashtagPrefix: "Quest"))
        def challenge1 = new QuestChallenge(id: 1L,
                endDate: TimeUtils.getDate(2014, 6, 16),
                defaultPoints: 10,
                hashtagSuffix: "2",
                contest: contest)
        def notification1 = new Notification(id: 1L,
                authorUsername: "username",
                timestamp: TimeUtils.getDate(2014, 6, 15),
                notificationType: NotificationType.TWITTER)
        def questParticipant = new QuestParticipant(id: 1L)
        def questSubmission = new QuestSubmission(id: 1L)

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge1])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification1])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, "username"))
                .thenReturn(questParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(challenge1, questParticipant))
                .thenReturn(questSubmission)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsInstagram() {
        def contest = new Contest(id: 1L, hashtag: "C1", questConfiguration: new QuestConfiguration(hashtagPrefix: "Quest"))
        def challenge1 = new QuestChallenge(id: 1L,
                endDate: TimeUtils.getDate(2014, 6, 16),
                defaultPoints: 10,
                hashtagSuffix: "2",
                contest: contest)
        def notification1 = new Notification(id: 1L,
                authorUsername: "username",
                timestamp: TimeUtils.getDate(2014, 6, 15),
                notificationType: NotificationType.INSTAGRAM)
        def questParticipant = new QuestParticipant(id: 1L)
        def questSubmission = new QuestSubmission(id: 1L)

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge1])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification1])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantInstagramUsernameIgnoreCase(contest, "username"))
                .thenReturn(questParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(challenge1, questParticipant))
                .thenReturn(questSubmission)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsVine() {
        def contest = new Contest(id: 1L, hashtag: "C1", questConfiguration: new QuestConfiguration(hashtagPrefix: "Quest"))
        def challenge1 = new QuestChallenge(id: 1L,
                endDate: TimeUtils.getDate(2014, 6, 16),
                defaultPoints: 10,
                hashtagSuffix: "2",
                contest: contest)
        def notification1 = new Notification(id: 1L,
                authorUsername: "username",
                timestamp: TimeUtils.getDate(2014, 6, 15),
                notificationType: NotificationType.VINE)
        def questParticipant = new QuestParticipant(id: 1L)
        def questSubmission = new QuestSubmission(id: 1L)

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge1])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification1])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantVineUsernameIgnoreCase(contest, "username"))
                .thenReturn(questParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(challenge1, questParticipant))
                .thenReturn(questSubmission)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsVineNonExisting() {
        def contest = new Contest(id: 1L, hashtag: "C1", questConfiguration: new QuestConfiguration(hashtagPrefix: "Quest"))
        def challenge1 = new QuestChallenge(id: 1L,
                endDate: TimeUtils.getDate(2014, 6, 16),
                defaultPoints: 10,
                hashtagSuffix: "2",
                contest: contest)
        def notification1 = new Notification(id: 1L,
                authorUsername: "username",
                timestamp: TimeUtils.getDate(2014, 6, 15),
                notificationType: NotificationType.VINE)
        def contestParticipant = new ContestParticipant(id: 1L)
        def questSubmission = new QuestSubmission(id: 1L)

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge1])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification1])
        Mockito.when(questParticipantRepository.findByContestAndContestParticipantVineUsernameIgnoreCase(contest, "username"))
                .thenReturn(null)
        Mockito.when(contestParticipantRepository.findByVineUsernameIgnoreCase("username"))
                .thenReturn(contestParticipant)
        Mockito.when(submissionRepository.findByChallengeAndParticipant(challenge1, Matchers.any(QuestParticipant.class)))
                .thenReturn(questSubmission)

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(1)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsPostedAfterDeadline() {
        def contest = new Contest(id: 1L, hashtag: "C1", questConfiguration: new QuestConfiguration(hashtagPrefix: "Quest"))
        def challenge1 = new QuestChallenge(id: 1L,
                endDate: TimeUtils.getDate(2014, 6, 16),
                defaultPoints: 10,
                hashtagSuffix: "2",
                contest: contest)
        def notification1 = new Notification(id: 1L,
                authorUsername: "username",
                timestamp: TimeUtils.getDate(2014, 6, 17),
                notificationType: NotificationType.TWITTER)

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge1])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification1])

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(0)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessAllSubmissionsPostedRetweeted() {
        def contest = new Contest(id: 1L, hashtag: "C1", questConfiguration: new QuestConfiguration(hashtagPrefix: "Quest"))
        def challenge1 = new QuestChallenge(id: 1L,
                endDate: TimeUtils.getDate(2014, 6, 16),
                defaultPoints: 10,
                hashtagSuffix: "2",
                contest: contest)
        def notification1 = new Notification(id: 1L,
                authorUsername: "username",
                parentId: 2L,
                timestamp: TimeUtils.getDate(2014, 6, 15),
                notificationType: NotificationType.TWITTER)

        Mockito.when(challengeRepository.findByContest(contest)).thenReturn([challenge1])
        Mockito.when(notificationRepository.findByHashTagsAndContest("%|Quest2|%", "%|C1|%", contest))
                .thenReturn([notification1])

        questSubmissionService.processAllSubmissions(contest)

        Mockito.verify(submissionRepository, Mockito.times(0)).saveAndFlush(Matchers.any(QuestSubmission.class))
    }

    void testProcessRecentSubmissions() {

    }

    void testMoveVotingToNextRound() {

    }
}
