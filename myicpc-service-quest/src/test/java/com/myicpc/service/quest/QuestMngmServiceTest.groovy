package com.myicpc.service.quest

import com.myicpc.repository.quest.QuestChallengeRepository
import com.myicpc.repository.quest.QuestParticipantRepository
import com.myicpc.repository.quest.QuestSubmissionRepository
import com.myicpc.repository.social.NotificationRepository
import com.myicpc.service.publish.PublishService
import com.myicpc.service.validation.QuestChallengeValidator
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Unit tests for {@link QuestMngmService}
 *
 * @author Roman Smetana
 */
class QuestMngmServiceTest extends GroovyTestCase {
    @Mock
    private QuestChallengeRepository challengeRepository;

    @Mock
    private QuestParticipantRepository participantRepository;

    @Mock
    private QuestSubmissionRepository submissionRepository;

    @Mock
    private QuestChallengeValidator challengeValidator;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private PublishService publishService;

    @InjectMocks
    private QuestMngmService questMngmService;

    @Override
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this)
    }

    void testUpdateQuestChallenge() {

    }

    void testDeleteQuestChallenge() {

    }

    void testBulkParticipantUpdate() {

    }

    void testAcceptQuestSubmission() {

    }

    void testRejectQuestSubmission() {

    }

    void testGetQuestSubmissionStates() {

    }

    void testGetAllContestParticipantRoles() {

    }

    void testCreateNotificationsForNewQuestChallenges() {

    }
}
