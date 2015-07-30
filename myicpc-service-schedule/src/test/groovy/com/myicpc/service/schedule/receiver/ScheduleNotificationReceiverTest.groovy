package com.myicpc.service.schedule.receiver

import com.myicpc.dto.jms.JMSEvent
import com.myicpc.model.contest.Contest
import com.myicpc.repository.contest.ContestRepository
import com.myicpc.service.schedule.ScheduleMngmService
import org.mockito.*

/**
 * Unit tests for {@link ScheduleNotificationReceiver}
 *
 * @author Roman Smetana
 */
class ScheduleNotificationReceiverTest extends GroovyTestCase {

    @Mock
    private ScheduleMngmService scheduleMngmService;

    @Mock
    private ContestRepository contestRepository;

    @InjectMocks
    private ScheduleNotificationReceiver scheduleNotificationReceiver;

    @Override
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this)
    }

    void testProcessQuestEventScheduleOpen() {
        def contest = new Contest(id: 1L)
        def jmsEvent = new JMSEvent(contestId: 1L, eventType: JMSEvent.EventType.SCHEDULE_OPEN_EVENT)

        Mockito.when(contestRepository.findOne(1L)).thenReturn(contest)

        scheduleNotificationReceiver.processQuestEvent(jmsEvent)

        Mockito.verify(scheduleMngmService).createNonPublishedEventNotifications(Matchers.any(Contest.class))
    }

    void testProcessQuestEventNullJMSEvent() {
        scheduleNotificationReceiver.processQuestEvent(null)

        Mockito.verifyZeroInteractions(scheduleMngmService)
    }

    void testProcessQuestEventNullContest() {
        def jmsEvent = new JMSEvent(contestId: 1L, eventType: JMSEvent.EventType.SCHEDULE_OPEN_EVENT)

        Mockito.when(contestRepository.findOne(1L)).thenReturn(null)

        scheduleNotificationReceiver.processQuestEvent(jmsEvent)

        Mockito.verifyZeroInteractions(scheduleMngmService)
    }
}
