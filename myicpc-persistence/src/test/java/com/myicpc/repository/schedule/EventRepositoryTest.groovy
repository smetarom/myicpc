package com.myicpc.repository.schedule

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.myicpc.model.contest.Contest
import com.myicpc.model.schedule.Event
import com.myicpc.model.schedule.EventRole
import com.myicpc.model.schedule.Location
import com.myicpc.repository.AbstractRepositoryTest
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

/**
 * DbUnit tests for {@link EventRepository} and {@link EventRepositoryImpl}
 *
 * @author Roman Smetana
 */
@DatabaseSetup("classpath:dbunit/schedule/EventRepositoryTest.xml")
class EventRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void testFindByContestWithScheduleDayAndLocation() {
        def contest = new Contest(id: 1)
        def events = eventRepository.findByContestWithScheduleDayAndLocation(contest)

        Assert.assertEquals 6, events.size()
        events.each {
            Assert.assertEquals 1L, it.contest.id
        }
    }

    @Test
    void testFindByContestWithScheduleDayAndLocationNullContest() {
        def events = eventRepository.findByContestWithScheduleDayAndLocation(null)
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testFindByLocationWithScheduleDayAndLocation() {
        def location = new Location(id: 1)
        def events = eventRepository.findByLocationWithScheduleDayAndLocation(location)

        Assert.assertEquals 2, events.size()
    }

    @Test
    void testFindByLocationWithScheduleDayAndLocationNullContest() {
        def events = eventRepository.findByLocationWithScheduleDayAndLocation(null)
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testFindAllFutureEvents() {
        def date = getDate(2014, 6, 22)
        def contest = new Contest(id: 1)
        def events = eventRepository.findAllFutureEvents(date, contest)

        Assert.assertEquals 4, events.size()
        events.each {
            Assert.assertFalse date.after(it.endDate)
            Assert.assertEquals 1L, it.contest.id
        }
    }

    @Test
    void testFindAllFutureEventsNullDate() {
        def contest = new Contest(id: 1)
        def events = eventRepository.findAllFutureEvents(null, contest)
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testFindAllFutureEventsNullContest() {
        def date = getDate(2014, 6, 22)
        def events = eventRepository.findAllFutureEvents(date, null)
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testFindTimelineUpcomingEvents() {
        def fromDate = getDateTime(2014, 6, 22, 9, 0)
        def toDate = getDateTime(2014, 6, 22, 12, 0)
        def events = eventRepository.findTimelineUpcomingEvents(fromDate, toDate, new Contest(id: 1L))

        Assert.assertEquals 1, events.size()
        Assert.assertEquals 1, events[0].id
        events.each {
            Assert.assertFalse fromDate.after(it.endDate)
            Assert.assertFalse toDate.before(it.endDate)
            Assert.assertEquals 1L, it.contest.id
        }
    }

    @Test
    void testFindTimelineUpcomingEventsNullFromDate() {
        def toDate = getDateTime(2014, 6, 22, 12, 0)
        def events = eventRepository.findTimelineUpcomingEvents(null, toDate, new Contest(id: 1L))
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testFindTimelineUpcomingEventsNullToDate() {
        def fromDate = getDateTime(2014, 6, 22, 9, 0)
        def events = eventRepository.findTimelineUpcomingEvents(fromDate, null, new Contest(id: 1L))
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testFindTimelineUpcomingEventsNullContest() {
        def fromDate = getDateTime(2014, 6, 22, 9, 0)
        def toDate = getDateTime(2014, 6, 22, 12, 0)
        def events = eventRepository.findTimelineUpcomingEvents(fromDate, toDate, null)
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testFindAllBetweenDates() {
        def fromDate = getDateTime(2014, 6, 22, 7, 0)
        def toDate = getDateTime(2014, 6, 22, 22, 0)
        def events = eventRepository.findAllBetweenDates(fromDate, toDate, new Contest(id: 1))

        Assert.assertEquals 2, events.size()
        events.each {
            Assert.assertFalse fromDate.after(it.startDate)
            Assert.assertFalse toDate.before(it.startDate)
            Assert.assertEquals 1L, it.contest.id
        }
    }

    @Test
    void testFindAllBetweenDatesNullStartDate() {
        def toDate = getDateTime(2014, 6, 22, 22, 0)
        def events = eventRepository.findAllBetweenDates(null, toDate, new Contest(id: 1))
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testFindAllBetweenDatesNullEndDate() {
        def fromDate = getDateTime(2014, 6, 22, 9, 0)
        def events = eventRepository.findAllBetweenDates(fromDate, null, new Contest(id: 1))
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testFindAllBetweenDatesNullContest() {
        def fromDate = getDateTime(2014, 6, 22, 9, 0)
        def toDate = getDateTime(2014, 6, 22, 22, 0)
        def events = eventRepository.findAllBetweenDates(fromDate, toDate, null)
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testCountByEventRole() {
        def eventRole = new EventRole(id: 1)
        def eventCount = eventRepository.countByEventRole(eventRole)
        Assert.assertEquals 3, eventCount
    }

    @Test
    void testCountByEventRoleNullEventRole() {
        def eventCount = eventRepository.countByEventRole(null)
        Assert.assertEquals 0, eventCount
    }

    @Test
    void testFindNonpublishedOpenEvents() {
        def date = getDate(2014, 6, 23)
        def events = eventRepository.findNonpublishedOpenEvents(date, new Contest(id: 1L))

        Assert.assertEquals 3, events.size()
        for (Event event : events) {
            Assert.assertFalse event.published
            Assert.assertTrue date.after(event.startDate)
            Assert.assertEquals 1L, event.contest.id
        }
    }

    @Test
    void testFindNonpublishedOpenEventsNullDate() {
        def events = eventRepository.findNonpublishedOpenEvents(null, new Contest(id: 1L))
        Assert.assertTrue events.isEmpty()
    }

    @Test
    void testFindNonpublishedOpenEventsNullContest() {
        def date = getDate(2014, 6, 23)
        def events = eventRepository.findNonpublishedOpenEvents(date, null)
        Assert.assertTrue events.isEmpty()
    }

    @Test
    @Transactional
    void testFindEventsForRolesNullRoles() {
        def contest = new Contest(id: 2L)
        def roleIds = null
        def now = getDateTime(2014, 6, 21, 5, 0)
        def limit = getDateTime(2014, 6, 23, 5, 0)
        def sortedResult = true
        def events = eventRepository.findEventsForRoles(roleIds, now, limit, contest, sortedResult)

        Assert.assertEquals 2, events.size()
        events.each {
            Assert.assertTrue it.roles.isEmpty()
        }
    }

    @Test
    @Transactional
    void testFindEventsForRolesEmptyRoles() {
        def contest = new Contest(id: 2L)
        def roleIds = new String[0]
        def now = getDateTime(2014, 5, 21, 15, 0)
        def limit = getDateTime(2014, 8, 23, 5, 0)
        def sortedResult = true
        def events = eventRepository.findEventsForRoles(roleIds, now, limit, contest, sortedResult)

        Assert.assertEquals 2, events.size()
        events.each {
            Assert.assertTrue it.roles.isEmpty()
        }
    }

    @Test
    @Transactional
    void testFindEventsForRolesNullNow() {
        def contest = new Contest(id: 2L)
        String[] roleIds = ["10", "11"]
        def now = null
        def limit = getDateTime(2014, 6, 22, 23, 0)
        def sortedResult = true
        def events = eventRepository.findEventsForRoles(roleIds, now, limit, contest, sortedResult)

        Assert.assertEquals 5, events.size()
        events.each {
            Assert.assertTrue it.roles.isEmpty() || it.roles.any {it.id == 10} || it.roles.any {it.id == 11}
            Assert.assertFalse limit.before(it.startDate)
        }
    }

    @Test
    @Transactional
    void testFindEventsForRolesNullLimit() {
        def contest = new Contest(id: 2L)
        String[] roleIds = ["10", "11"]
        def now = getDateTime(2014, 6, 21, 22, 0)
        def limit = null
        def sortedResult = true
        def events = eventRepository.findEventsForRoles(roleIds, now, limit, contest, sortedResult)

        Assert.assertEquals 3, events.size()
        events.each {
            Assert.assertTrue it.roles.isEmpty() || it.roles.any {it.id == 10} || it.roles.any {it.id == 11}
            Assert.assertFalse now.after(it.endDate)
        }
    }

    @Test
    void testFindEventsForRolesNullContest() {
        def contest = null
        String[] roleIds = ["10", "11"]
        def now = getDateTime(2014, 6, 23, 5, 0)
        def limit = getDateTime(2014, 6, 23, 5, 0)
        def sortedResult = true
        def events = eventRepository.findEventsForRoles(roleIds, now, limit, contest, sortedResult)

        Assert.assertTrue events.isEmpty()
    }

    @Test
    @Transactional
    void testFindEventsForRolesTrueSortedResult() {
        def contest = new Contest(id: 2L)
        String[] roleIds = ["10", "11", "12", "13"]
        def now = null
        def limit = null
        def sortedResult = true
        def events = eventRepository.findEventsForRoles(roleIds, now, limit, contest, sortedResult)

        Assert.assertEquals 6, events.size()

        def tmpDate = getDate(2014, 1, 1)
        events.each {
            Assert.assertTrue it.roles.isEmpty() ||
                    it.roles.any {it.id == 10} ||
                    it.roles.any {it.id == 11} ||
                    it.roles.any {it.id == 12} ||
                    it.roles.any {it.id == 13}
            Assert.assertFalse tmpDate.after(it.startDate)
            tmpDate = it.startDate
        }
    }
}
