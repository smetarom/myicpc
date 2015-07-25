package com.myicpc.service.quest.receiver

import com.myicpc.dto.jms.JMSEvent
import com.myicpc.model.contest.Contest
import com.myicpc.repository.contest.ContestRepository
import com.myicpc.service.quest.QuestMngmService
import com.myicpc.service.quest.QuestSubmissionService
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Unit tests for {@link QuestNotificationReceiver}
 *
 * @author Roman Smetana
 */
class QuestNotificationReceiverTest extends GroovyTestCase {
    @Mock
    private QuestMngmService questMngmService

    @Mock
    private QuestSubmissionService questSubmissionService

    @Mock
    private ContestRepository contestRepository

    @InjectMocks
    private QuestNotificationReceiver questNotificationReceiver

    @Override
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this)
    }

    void testProcessQuestEventNullEvent() {
        questNotificationReceiver.processQuestEvent(null)
    }

    void testProcessQuestEventQuestChallenge() {
        def jmsEvent = new JMSEvent(contestId: 1L, eventType: JMSEvent.EventType.QUEST_CHALLENGE)
        def contest = new Contest(id: 1L)
        Mockito.when(contestRepository.findOne(1L)).thenReturn(contest)

        questNotificationReceiver.processQuestEvent(jmsEvent)

        Mockito.verify(questMngmService).createNotificationsForNewQuestChallenges(contest)
    }

    void testProcessQuestEventQuestSubmissions() {
        def jmsEvent = new JMSEvent(contestId: 1L, eventType: JMSEvent.EventType.QUEST_SUBMISSIONS)
        def contest = new Contest(id: 1L)
        Mockito.when(contestRepository.findOne(1L)).thenReturn(contest)

        questNotificationReceiver.processQuestEvent(jmsEvent)

        Mockito.verify(questSubmissionService).processRecentSubmissions(contest)
    }

    void testProcessQuestEventQuestSubmissionsFull() {
        def jmsEvent = new JMSEvent(contestId: 1L, eventType: JMSEvent.EventType.QUEST_SUBMISSIONS_FULL)
        def contest = new Contest(id: 1L)
        Mockito.when(contestRepository.findOne(1L)).thenReturn(contest)

        questNotificationReceiver.processQuestEvent(jmsEvent)

        Mockito.verify(questSubmissionService).processAllSubmissions(contest)
    }
}
