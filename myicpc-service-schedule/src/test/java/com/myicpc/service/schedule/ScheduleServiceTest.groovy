package com.myicpc.service.schedule

import com.myicpc.commons.utils.TimeUtils
import com.myicpc.model.contest.Contest
import com.myicpc.model.schedule.Event
import com.myicpc.model.schedule.EventRole
import com.myicpc.model.schedule.Location
import com.myicpc.model.schedule.ScheduleDay
import com.myicpc.repository.schedule.EventRepository
import com.myicpc.repository.schedule.EventRoleRepository
import org.junit.Assert
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Unit tests for {@link ScheduleService}
 *
 * @author Roman Smetana
 */
class ScheduleServiceTest extends GroovyTestCase {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventRoleRepository eventRoleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    void testGetEventByIdOrCodeNumber() {
        def contest = new Contest(id: 1L)
        def event = new Event(id: 1L, code: "code", contest: contest, roles: [])
        Mockito.when(eventRepository.findOne(1L)).thenReturn(event)

        def returnedEvent = scheduleService.getEventByIdOrCode("1", contest)

        Assert.assertEquals event, returnedEvent
        Mockito.verify(eventRepository, Mockito.times(1)).findOne(1L)
    }

    void testGetEventByIdOrCodeString() {
        def contest = new Contest(id: 1L)
        def event = new Event(id: 1L, code: "code", contest: contest, roles: [])
        Mockito.when(eventRepository.findByCodeAndContest("code", contest)).thenReturn(event)

        def returnedEvent = scheduleService.getEventByIdOrCode("code", contest)

        Assert.assertEquals event, returnedEvent
        Mockito.verify(eventRepository, Mockito.times(1)).findByCodeAndContest("code", contest)
    }

    void testGetEventByIdOrCodeNullCode() {
        def contest = new Contest(id: 1L)
        def event = new Event(id: 1L, code: "code", contest: contest, roles: [])
        Mockito.when(eventRepository.findByCodeAndContest("code", contest)).thenReturn(event)

        def returnedEvent = scheduleService.getEventByIdOrCode(null, contest)

        Assert.assertNull returnedEvent
    }

    void testGetEventByIdOrCodeNullContest() {
        Mockito.when(eventRepository.findByCodeAndContest("code", null)).thenReturn(null)

        def returnedEvent = scheduleService.getEventByIdOrCode("code", null)

        Assert.assertNull returnedEvent
    }

    void testGetCurrentContestSchedule() {
        def contest = new Contest(id: 1L)
        def date = new Date()
        Mockito.when(eventRepository.findAllFutureEvents(date, contest))
                .thenReturn(getSampleSchedule())

        def schedule = scheduleService.getCurrentContestSchedule(date, contest)
        verifySampleSchedule(schedule)
    }

    void testGetMyCurrentSchedule() {
        def contest = new Contest(id: 1L)
        def date = new Date()
        def roles = new String[0];
        Mockito.when(eventRepository.findEventsForRoles(roles, date, contest))
                .thenReturn(getSampleSchedule())

        def schedule = scheduleService.getMyCurrentSchedule(roles, date, contest)
        verifySampleSchedule(schedule)
    }

    void testGetEntireContestSchedule() {
        def contest = new Contest(id: 1L)
        Mockito.when(eventRepository.findByContestWithScheduleDayAndLocation(contest))
                .thenReturn(getSampleSchedule())

        def schedule = scheduleService.getEntireContestSchedule(contest)
        verifySampleSchedule(schedule)
    }

    void testGetScheduleEventsInLocation() {
        def location = new Location(id: 1L)
        Mockito.when(eventRepository.findByLocationWithScheduleDayAndLocation(location))
                .thenReturn(getSampleSchedule())

        def schedule = scheduleService.getScheduleEventsInLocation(location)
        verifySampleSchedule(schedule)
    }

    void testGetAllScheduleRoles() {
        def contest = new Contest(id: 1L)
        def eventRoles = [new EventRole(id: 1L), new EventRole(id: 2L), new EventRole(id: 3L)]
        Mockito.when(eventRoleRepository.findByContest(contest)).thenReturn(eventRoles)

        def rolesString = scheduleService.getAllScheduleRoles(contest)
        Assert.assertEquals "1,2,3", rolesString
    }

    void testGetAllScheduleRolesNullRoles() {
        def contest = new Contest(id: 1L)
        Mockito.when(eventRoleRepository.findByContest(contest)).thenReturn(null)

        def rolesString = scheduleService.getAllScheduleRoles(contest)
        Assert.assertNull rolesString
    }

    void testGetAllScheduleRolesEmptyRoles() {
        def contest = new Contest(id: 1L)
        Mockito.when(eventRoleRepository.findByContest(contest)).thenReturn([])

        def rolesString = scheduleService.getAllScheduleRoles(contest)
        Assert.assertNull rolesString
    }

    void testGetActiveEventRoleMapping() {
        def mapping = scheduleService.getActiveEventRoleMapping("1,23,43")

        Assert.assertNull mapping[0L]
        Assert.assertTrue mapping[1L]
        Assert.assertNull mapping[2L]
        Assert.assertTrue mapping[23L]
        Assert.assertTrue mapping[43L]
        Assert.assertNull mapping[50L]
    }

    void testGetActiveEventRoleMappingNullScheduleRoles() {
        def mapping = scheduleService.getActiveEventRoleMapping(null)

        Assert.assertTrue mapping.values().isEmpty()
    }

    void testGetUpcomingEvents() {
        def contest = new Contest(id: 1L)
        Mockito.when(eventRepository.findTimelineUpcomingEvents(Mockito.mock(Date.class), Mockito.mock(Date.class), contest)).thenReturn([])
        scheduleService.getUpcomingEvents(8, contest)
    }

    void testGetScheduleDaysJSON() {
        // TODO test JSON representation
    }

    private def getSampleSchedule() {
        def scheduleDay1 = new ScheduleDay(id: 1L, dayOrder: 1, name: "Day 1", date: TimeUtils.getDate(2014, 6, 21))
        def scheduleDay2 = new ScheduleDay(id: 2L, dayOrder: 2, name: "Day 2", date: TimeUtils.getDate(2014, 6, 22))

        def event1 = new Event(id: 1L, scheduleDay: scheduleDay1, startDate: TimeUtils.getDateTime(2014, 6, 21, 14, 0))
        def event2 = new Event(id: 2L, scheduleDay: scheduleDay1, startDate: TimeUtils.getDateTime(2014, 6, 21, 9, 0))
        def event3 = new Event(id: 3L, scheduleDay: scheduleDay1, startDate: TimeUtils.getDateTime(2014, 6, 21, 11, 0))
        def event4 = new Event(id: 4L, scheduleDay: scheduleDay2, startDate: TimeUtils.getDateTime(2014, 6, 22, 14, 0))
        def event5 = new Event(id: 5L, scheduleDay: scheduleDay2, startDate: TimeUtils.getDateTime(2014, 6, 22, 10, 0))
        def event6 = new Event(id: 6L, scheduleDay: scheduleDay2, startDate: TimeUtils.getDateTime(2014, 6, 22, 22, 0))
        def event7 = new Event(id: 7L, scheduleDay: scheduleDay2, startDate: TimeUtils.getDateTime(2014, 6, 22, 12, 0))

        return [event1, event2, event3, event4, event5, event6, event7]
    }

    private void verifySampleSchedule(List<ScheduleDay> scheduleDays) {
        Assert.assertEquals 2, scheduleDays.size()

        Assert.assertEquals 1L, scheduleDays[0].eventsChronologically[2].id
        Assert.assertEquals 2L, scheduleDays[0].eventsChronologically[0].id
        Assert.assertEquals 3L, scheduleDays[0].eventsChronologically[1].id

        Assert.assertEquals 4L, scheduleDays[1].eventsChronologically[2].id
        Assert.assertEquals 5L, scheduleDays[1].eventsChronologically[0].id
        Assert.assertEquals 6L, scheduleDays[1].eventsChronologically[3].id
        Assert.assertEquals 7L, scheduleDays[1].eventsChronologically[1].id
    }
}
