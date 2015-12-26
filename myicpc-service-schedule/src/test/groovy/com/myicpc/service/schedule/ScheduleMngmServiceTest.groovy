package com.myicpc.service.schedule

import com.myicpc.commons.utils.FormatUtils
import com.myicpc.commons.utils.TimeUtils
import com.myicpc.enums.NotificationType
import com.myicpc.model.contest.Contest
import com.myicpc.model.schedule.Event
import com.myicpc.model.schedule.EventRole
import com.myicpc.model.schedule.Location
import com.myicpc.model.schedule.ScheduleDay
import com.myicpc.model.social.Notification
import com.myicpc.repository.schedule.EventRepository
import com.myicpc.repository.schedule.EventRoleRepository
import com.myicpc.repository.schedule.LocationRepository
import com.myicpc.repository.schedule.ScheduleDayRepository
import com.myicpc.repository.social.NotificationRepository
import com.myicpc.service.exception.BusinessValidationException
import com.myicpc.service.notification.NotificationService
import com.myicpc.service.publish.PublishService
import com.myicpc.service.schedule.dto.EditScheduleDTO
import com.myicpc.service.validation.EventRoleValidator
import com.myicpc.service.validation.EventValidator
import com.myicpc.service.validation.LocationValidator
import com.myicpc.service.validation.ScheduleDayValidator
import org.junit.Assert
import org.mockito.*
import org.springframework.mock.web.MockMultipartFile

import javax.validation.ValidationException

/**
 * Unit tests for {@link ScheduleMngmService}
 *
 * @author Roman Smetana
 */
class ScheduleMngmServiceTest extends GroovyTestCase {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationValidator locationValidator;

    @Mock
    private ScheduleDayRepository scheduleDayRepository;

    @Mock
    private ScheduleDayValidator scheduleDayValidator;

    @Mock
    private EventRoleRepository eventRoleRepository;

    @Mock
    private EventRoleValidator eventRoleValidator;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private PublishService publishService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private EventValidator eventValidator;

    @InjectMocks
    private ScheduleMngmService scheduleMngmService;

    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    void testSaveEvent() {
        def contest = new Contest(id: 1L)
        def event = new Event(id: 1L,
                name: "Event 1",
                startDate: TimeUtils.getDate(2014, 6, 21),
                endDate: TimeUtils.getDateTime(2014, 6, 21, 18, 0),
                location: new Location(id: 1L),
                roles: [new EventRole(name: "Role 1"), new EventRole(name: "Role 2")],
                thingsToBring: "Bring everything",
                contest: contest
        )
        def notification = new Notification()
        Mockito.when(eventRepository.save(event)).then(AdditionalAnswers.returnsFirstArg())
        Mockito.when(notificationRepository.findByContestAndEntityIdAndNotificationType(contest, 1L, NotificationType.SCHEDULE_EVENT_OPEN))
                .thenReturn([notification])

        def updatedEvent = scheduleMngmService.saveEvent(event)
        Assert.assertEquals updatedEvent, event
    }

    void testDeleteEvent() {
        def contest = new Contest(id: 1L)
        def event = new Event(id: 1L, contest: contest)

        scheduleMngmService.deleteEvent(event)

        Mockito.verify(notificationRepository).deleteByEntityIdAndNotificationType(event.id, NotificationType.SCHEDULE_EVENT_OPEN, contest)
        Mockito.verify(eventRepository).delete(Mockito.any(Event.class))
    }

    void testCreateEditSchedule() {
        def contest = new Contest(id: 1L)
        def events = [
                new Event(id: 1L, startDate: TimeUtils.getDate(2014, 6, 22)),
                new Event(id: 2L, startDate: TimeUtils.getDate(2014, 6, 23)),
                new Event(id: 3L, startDate: TimeUtils.getDate(2014, 6, 21)),
        ]
        Mockito.when(eventRepository.findByContestWithScheduleDayAndLocation(contest)).thenReturn(events)
        def editScheduleDTO = scheduleMngmService.createEditSchedule(contest)

        Assert.assertEquals 3, editScheduleDTO.events.size()
        def tmpDate = TimeUtils.getDate(1990, 1, 1)
        editScheduleDTO.events.each {
            Assert.assertFalse tmpDate.after(it.startDate)
            tmpDate = it.startDate
        }

    }

    void testCreateEditScheduleNullContest() {
        Mockito.when(eventRepository.findByContestWithScheduleDayAndLocation(null)).thenReturn([])
        def editScheduleDTO = scheduleMngmService.createEditSchedule(null)

        Assert.assertNotNull editScheduleDTO
        Assert.assertTrue editScheduleDTO.events.isEmpty()
    }

    void testSaveBulkEdit() {
        def events = [
                new Event(id: 1L, startDate: TimeUtils.getDate(2014, 6, 22)),
                new Event(id: 2L, startDate: TimeUtils.getDate(2014, 6, 23)),
                new Event(id: 3L, startDate: TimeUtils.getDate(2014, 6, 21)),
        ]
        def editSchedule =  new EditScheduleDTO(events)
        scheduleMngmService.saveBulkEdit(editSchedule)
        Mockito.verify(eventRepository, Mockito.times(3)).save(Matchers.any(Event.class))
    }

    void testSaveBulkEditNullContest() {
        scheduleMngmService.saveBulkEdit(null)
        Mockito.verifyZeroInteractions(eventRepository)
    }

    void testSaveBulkEditNullEvents() {
        scheduleMngmService.saveBulkEdit(new EditScheduleDTO(null))
        Mockito.verifyZeroInteractions(eventRepository)
    }

    void testSaveLocation() {
        def location = new Location(id: 1L)
        Mockito.when(locationRepository.save(location)).then(AdditionalAnswers.returnsFirstArg())

        def updatedLocation = scheduleMngmService.saveLocation(location)

        Assert.assertEquals location, updatedLocation
    }

    void testSaveLocationInvalid() {
        def location = new Location(id: 1L)

        Mockito.when(locationValidator.validate(location)).thenThrow(BusinessValidationException.class)

        shouldFail(BusinessValidationException.class) {
            scheduleMngmService.saveLocation(location)
        }
        Mockito.verifyZeroInteractions(locationRepository)
    }

    void testDeleteLocation() {
        def location = new Location(id: 1L)
        Mockito.when(eventRepository.countByLocation(location)).thenReturn(0L)

        scheduleMngmService.deleteLocation(location)

        Mockito.verify(locationRepository).delete(Matchers.any(Location.class))
    }

    void testDeleteLocationInvalid() {
        def location = new Location(id: 1L)
        Mockito.when(eventRepository.countByLocation(location)).thenReturn(2L)

        shouldFail(BusinessValidationException.class) {
            scheduleMngmService.deleteLocation(location)
        }

        Mockito.verifyZeroInteractions(locationRepository)
    }

    void testSaveScheduleDay() {
        def scheduleDay = new ScheduleDay(id: 1L)
        Mockito.when(scheduleDayRepository.save(scheduleDay)).then(AdditionalAnswers.returnsFirstArg())

        def updatedScheduleDay = scheduleMngmService.saveScheduleDay(scheduleDay)

        Assert.assertEquals scheduleDay, updatedScheduleDay
    }

    void testSaveScheduleDayInvalid() {
        def scheduleDay = new ScheduleDay(id: 1L)
        Mockito.when(scheduleDayValidator.validate(scheduleDay)).thenThrow(BusinessValidationException.class)

        shouldFail(BusinessValidationException.class) {
            scheduleMngmService.saveScheduleDay(scheduleDay)
        }
        Mockito.verifyZeroInteractions(scheduleDayRepository)
    }

    void testDeleteScheduleDay() {
        def scheduleDay = new ScheduleDay(id: 1L)
        Mockito.when(eventRepository.countByScheduleDay(scheduleDay)).thenReturn(0L)

        scheduleMngmService.deleteScheduleDay(scheduleDay)

        Mockito.verify(scheduleDayRepository).delete(Matchers.any(ScheduleDay.class))
    }

    void testDeleteScheduleDayInvalid() {
        def scheduleDay = new ScheduleDay(id: 1L)
        Mockito.when(eventRepository.countByScheduleDay(scheduleDay)).thenReturn(1L)

        shouldFail(BusinessValidationException.class) {
            scheduleMngmService.deleteScheduleDay(scheduleDay)
        }

        Mockito.verifyZeroInteractions(scheduleDayRepository)
    }

    void testSaveEventRole() {
        def eventRole = new EventRole(id: 1L)
        Mockito.when(eventRoleRepository.save(eventRole)).then(AdditionalAnswers.returnsFirstArg())

        def updatedEventRole = scheduleMngmService.saveEventRole(eventRole)

        Assert.assertEquals eventRole, updatedEventRole
    }

    void testSaveEventRoleInvalid() {
        def eventRole = new EventRole(id: 1L)
        Mockito.when(eventRoleValidator.validate(eventRole)).thenThrow(BusinessValidationException.class)

        shouldFail(BusinessValidationException.class) {
            scheduleMngmService.saveEventRole(eventRole)
        }
        Mockito.verifyZeroInteractions(eventRoleRepository)
    }

    void testDeleteEventRole() {
        def eventRole = new EventRole(id: 1L)
        Mockito.when(eventRepository.countByEventRole(eventRole)).thenReturn(0L)

        scheduleMngmService.deleteEventRole(eventRole)

        Mockito.verify(eventRoleRepository).delete(Matchers.any(EventRole.class))
    }

    void testDeleteEventRoleInvalid() {
        def eventRole = new EventRole(id: 1L)
        Mockito.when(eventRepository.countByEventRole(eventRole)).thenReturn(1L)

        shouldFail(BusinessValidationException.class) {
            scheduleMngmService.deleteEventRole(eventRole)
        }

        Mockito.verifyZeroInteractions(eventRoleRepository)
    }

    void testCreateNonPublishedEventNotifications() {
        def contest = new Contest(id: 1L, timeDifference: 0)
        def event1 = new Event(id: 1L, startDate: TimeUtils.getDateTime(2014, 6, 21, 12, 0), endDate: TimeUtils.getDateTime(2014, 6, 21, 14, 0), contest: contest)
        def event2 = new Event(id: 2L, startDate: TimeUtils.getDateTime(2014, 6, 21, 16, 0), endDate: TimeUtils.getDateTime(2014, 6, 21, 19, 0), contest: contest)
        Mockito.when(eventRepository.findNonpublishedOpenEvents(Matchers.any(Date.class), Matchers.any(Contest.class)))
                .thenReturn([event1, event2])
        Mockito.when(notificationRepository.save(Matchers.any(Notification.class))).then(AdditionalAnswers.returnsFirstArg())

        scheduleMngmService.createNonPublishedEventNotifications(contest)

        Mockito.verify(notificationRepository, Mockito.times(2)).save(Matchers.any(Notification.class))
        Mockito.verify(publishService, Mockito.times(2)).broadcastNotification(Matchers.any(Notification.class), Matchers.any(Contest.class))
    }

    void testImportSchedule() {

    }

    void testParseScheduleRoleImportFile() {
        def fileContent = "Role1\n" +
                "Role2"
        def file = new MockMultipartFile("roles.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(eventRoleRepository.findByNameAndContest("Role1", contest)).thenReturn(new EventRole(id: 1L))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role2", contest)).thenReturn(null)

        scheduleMngmService.parseScheduleRoleImportFile(file, contest)

        Mockito.verify(eventRoleRepository, Mockito.times(2)).save(Matchers.any(EventRole.class))
    }

    void testParseScheduleRoleImportFileNullFile() {
        def contest = new Contest(id: 1L)

        scheduleMngmService.parseScheduleRoleImportFile(null, contest)
    }

    void testParseScheduleRoleImportFileNullContest() {
        def fileContent = "Role1\n" +
                "Role2"
        def file = new MockMultipartFile("roles.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))

        scheduleMngmService.parseScheduleRoleImportFile(file, null)
    }

    void testParseScheduleRoleImportFileInvalidData() {
        def fileContent = "Role1"
        def file = new MockMultipartFile("roles.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)
        def role = new EventRole(id: 1L)

        Mockito.when(eventRoleRepository.findByNameAndContest("Role1", contest)).thenReturn(role)
        Mockito.when(eventRoleValidator.validate(role)).thenThrow(BusinessValidationException.class)

        shouldFail(BusinessValidationException.class) {
            scheduleMngmService.parseScheduleRoleImportFile(file, contest)
        }
    }

    void testParseScheduleDayImportFile() {
        def fileContent = "1,Day 1,6/21/14,Event description\n" +
                "2,Day 2,6/22/14"
        def file = new MockMultipartFile("days.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(1, contest)).thenReturn(new ScheduleDay(id: 1L))
        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(2, contest)).thenReturn(null)

        scheduleMngmService.parseScheduleDayImportFile(file, contest)

        Mockito.verify(scheduleDayRepository, Mockito.times(2)).save(Matchers.any(ScheduleDay.class))
    }

    void testParseScheduleDayImportFileInvalidDayOrder() {
        def fileContent = "1,Day 1,6/21/14\n" +
                "order,Day 2,6/22/14"
        def file = new MockMultipartFile("days.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(1, contest)).thenReturn(new ScheduleDay(id: 1L))
        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(2, contest)).thenReturn(null)

        shouldFail(ValidationException.class) {
            scheduleMngmService.parseScheduleDayImportFile(file, contest)
        }

        Mockito.verify(scheduleDayRepository, Mockito.times(1)).save(Matchers.any(ScheduleDay.class))
    }

    void testParseScheduleDayImportFileInvalidDate() {
        def fileContent = "1,Day 1,date\n" +
                "2,Day 2,6/22/14"
        def file = new MockMultipartFile("days.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(1, contest)).thenReturn(new ScheduleDay(id: 1L))
        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(2, contest)).thenReturn(null)

        shouldFail(ValidationException.class) {
            scheduleMngmService.parseScheduleDayImportFile(file, contest)
        }
    }

    void testParseScheduleDayImportFileInvalidData() {
        def fileContent = "1,Day 1,6/21/14"
        def file = new MockMultipartFile("days.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)
        def scheduleDay = new ScheduleDay(id: 1L)

        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(1, contest)).thenReturn(scheduleDay)
        Mockito.when(scheduleDayValidator.validate(scheduleDay)).thenThrow(BusinessValidationException.class)

        shouldFail(BusinessValidationException.class) {
            scheduleMngmService.parseScheduleDayImportFile(file, contest)
        }
    }

    void testParseScheduleDayImportFileNullFile() {
        def contest = new Contest(id: 1L)

        scheduleMngmService.parseScheduleDayImportFile(null, contest)

        Mockito.verifyZeroInteractions(scheduleDayRepository)
    }

    void testParseScheduleDayImportFileNullContest() {
        def fileContent = "1,Day 1,6/21/14,Event description\n" +
                "2,Day 2,6/22/14"
        def file = new MockMultipartFile("days.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))

        scheduleMngmService.parseScheduleDayImportFile(file, null)

        Mockito.verifyZeroInteractions(scheduleDayRepository)
    }

    void testParseLocationImportFile() {
        def fileContent = "Location 1,location1,https://www.google.com/maps/1\n" +
                "Location 2,location2,"
        def file = new MockMultipartFile("locations.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(locationRepository.findByCodeAndContest("location1", contest)).thenReturn(new Location(id: 1L))
        Mockito.when(locationRepository.findByCodeAndContest("location2", contest)).thenReturn(null)

        scheduleMngmService.parseLocationImportFile(file, contest)

        Mockito.verify(locationRepository, Mockito.times(2)).save(Matchers.any(ScheduleDay.class))
    }

    void testParseLocationImportFileInvalidData() {
        def fileContent = "Location 1,location1,https://www.google.com/maps/1"
        def file = new MockMultipartFile("locations.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)
        def location = new Location(id: 1L)

        Mockito.when(locationRepository.findByCodeAndContest("location1", contest)).thenReturn(location)
        Mockito.when(locationValidator.validate(location)).thenThrow(BusinessValidationException.class)

        shouldFail(BusinessValidationException.class) {
            scheduleMngmService.parseLocationImportFile(file, contest)
        }
    }

    void testParseLocationImportFileNullFile() {
        def contest = new Contest(id: 1L)

        scheduleMngmService.parseLocationImportFile(null, contest)

        Mockito.verifyZeroInteractions(locationRepository)
    }

    void testParseLocationImportFileNullContest() {
        def fileContent = "Location 1,location1,https://www.google.com/maps/1\n" +
                "Location 2,location2,"
        def file = new MockMultipartFile("locations.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))

        scheduleMngmService.parseLocationImportFile(file, null)

        Mockito.verifyZeroInteractions(locationRepository)
    }

    void testParseEventImportFile() {
        def fileContent = "Event 1,event1,Event1,Event1,6/21/14 19:00,6/21/14 21:00,1,location1,Role1;Role2,Description,Thing to bring\n" +
                "Event 2,event2,Event2,,6/21/14 19:00,6/21/14 21:00,1,location2,Role1;Role2,Description,Thing to bring\n" +
                "Event 3,event3,Event3,Event3,6/21/14 19:00,6/21/14 21:00,2,location2,,Description,Thing to bring\n" +
                "Event 4,event4,Event4,Event4,6/21/14 19:00,6/21/14 21:00,2,location2,Role1;Role2,,Thing to bring\n" +
                "Event 5,event5,Event5,Event5,6/21/14 19:00,6/21/14 21:00,1,location1,Role1;Role2,Description,\n" +
                "Event 6,event6,Event6,Event6,6/21/14 19:00,6/21/14 21:00,2,location1,Role1;Role2,Description\n" +
                "Event 7,event7,Event7,Event7,6/21/14 19:00,6/21/14 21:00,1,location2,Role1;Role2\n" +
                "Event 8,event8,Event8,Event8,6/21/14 19:00,6/21/14 21:00,2,location1\n" +
                "Event 9,event9,Event9,,6/21/14 19:00,6/21/14 21:00,2,location1\n"
        def file = new MockMultipartFile("events.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(eventRepository.findByCodeAndContest("event1", contest)).thenReturn(new Event(id: 1L))
        Mockito.when(eventRepository.findByCodeAndContest("event2", contest)).thenReturn(new Event(id: 1L))
        Mockito.when(eventRepository.findByCodeAndContest("event3", contest)).thenReturn(null)
        Mockito.when(eventRepository.findByCodeAndContest("event4", contest)).thenReturn(null)
        Mockito.when(eventRepository.findByCodeAndContest("event5", contest)).thenReturn(null)
        Mockito.when(eventRepository.findByCodeAndContest("event6", contest)).thenReturn(null)
        Mockito.when(eventRepository.findByCodeAndContest("event7", contest)).thenReturn(null)
        Mockito.when(eventRepository.findByCodeAndContest("event8", contest)).thenReturn(null)
        Mockito.when(eventRepository.findByCodeAndContest("event9", contest)).thenReturn(null)

        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(1, contest)).thenReturn(new ScheduleDay(id: 1L))
        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(2, contest)).thenReturn(new ScheduleDay(id: 2L))

        Mockito.when(locationRepository.findByCodeAndContest("location1", contest)).thenReturn(new Location(id: 1L))
        Mockito.when(locationRepository.findByCodeAndContest("location2", contest)).thenReturn(new Location(id: 2L))

        Mockito.when(eventRoleRepository.findByNameAndContest("Role1", contest)).thenReturn(new EventRole(id: 1L, name: "Role1"))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role2", contest)).thenReturn(new EventRole(id: 2L, name: "Role2"))

        scheduleMngmService.parseEventImportFile(file, contest)

        Mockito.verify(eventRepository, Mockito.times(9)).save(Matchers.any(Event.class))
    }

    void testParseEventImportFileInvalidStartDate() {
        def fileContent = "Event 1,event1,Event1,Event1,date,6/21/14 21:00,1,location1,Role1;Role2,Description,Thing to bring"
        def file = new MockMultipartFile("events.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(eventRepository.findByCodeAndContest("event1", contest)).thenReturn(new Event(id: 1L))

        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(1, contest)).thenReturn(new ScheduleDay(id: 1L))
        Mockito.when(locationRepository.findByCodeAndContest("location1", contest)).thenReturn(new Location(id: 1L))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role1", contest)).thenReturn(new EventRole(id: 1L, name: "Role1"))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role2", contest)).thenReturn(new EventRole(id: 2L, name: "Role2"))

        shouldFail(ValidationException.class) {
            scheduleMngmService.parseEventImportFile(file, contest)
        }

        Mockito.verify(eventRepository, Mockito.times(0)).save(Matchers.any(Event.class))
    }

    void testParseEventImportFileInvalidEndDate() {
        def fileContent = "Event 1,event1,Event1,Event1,6/21/14 21:00,date,1,location1,Role1;Role2,Description,Thing to bring"
        def file = new MockMultipartFile("events.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(eventRepository.findByCodeAndContest("event1", contest)).thenReturn(new Event(id: 1L))

        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(1, contest)).thenReturn(new ScheduleDay(id: 1L))
        Mockito.when(locationRepository.findByCodeAndContest("location1", contest)).thenReturn(new Location(id: 1L))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role1", contest)).thenReturn(new EventRole(id: 1L, name: "Role1"))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role2", contest)).thenReturn(new EventRole(id: 2L, name: "Role2"))

        shouldFail(ValidationException.class) {
            scheduleMngmService.parseEventImportFile(file, contest)
        }

        Mockito.verify(eventRepository, Mockito.times(0)).save(Matchers.any(Event.class))
    }

    void testParseEventImportFileInvalidDayOrder() {
        def fileContent = "Event 1,event1,Event1,Event1,6/21/14 19:00,6/21/14 21:00,order,location1,Role1;Role2,Description,Thing to bring"
        def file = new MockMultipartFile("events.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(eventRepository.findByCodeAndContest("event1", contest)).thenReturn(new Event(id: 1L))

        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(1, contest)).thenReturn(new ScheduleDay(id: 1L))
        Mockito.when(locationRepository.findByCodeAndContest("location1", contest)).thenReturn(new Location(id: 1L))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role1", contest)).thenReturn(new EventRole(id: 1L, name: "Role1"))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role2", contest)).thenReturn(new EventRole(id: 2L, name: "Role2"))

        shouldFail(ValidationException.class) {
            scheduleMngmService.parseEventImportFile(file, contest)
        }

        Mockito.verify(eventRepository, Mockito.times(0)).save(Matchers.any(Event.class))
    }

    void testParseEventImportFileNotExistingScheduleDay() {
        def fileContent = "Event 1,event1,Event1,Event1,6/21/14 19:00,6/21/14 21:00,1,location1,Role1;Role2,Description,Thing to bring"
        def file = new MockMultipartFile("events.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(eventRepository.findByCodeAndContest("event1", contest)).thenReturn(new Event(id: 1L))

        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(1, contest)).thenReturn(null)
        Mockito.when(locationRepository.findByCodeAndContest("location1", contest)).thenReturn(new Location(id: 1L))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role1", contest)).thenReturn(new EventRole(id: 1L, name: "Role1"))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role2", contest)).thenReturn(new EventRole(id: 2L, name: "Role2"))

        shouldFail(ValidationException.class) {
            scheduleMngmService.parseEventImportFile(file, contest)
        }

        Mockito.verify(eventRepository, Mockito.times(0)).save(Matchers.any(Event.class))
    }

    void testParseEventImportFileNotExistingEventRole() {
        def fileContent = "Event 1,event1,Event1,Event1,6/21/14 19:00,6/21/14 21:00,1,location1,Role1;Role2,Description,Thing to bring"
        def file = new MockMultipartFile("events.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))
        def contest = new Contest(id: 1L)

        Mockito.when(eventRepository.findByCodeAndContest("event1", contest)).thenReturn(new Event(id: 1L))

        Mockito.when(scheduleDayRepository.findByDayOrderAndContest(1, contest)).thenReturn(new ScheduleDay(id: 1L))
        Mockito.when(locationRepository.findByCodeAndContest("location1", contest)).thenReturn(new Location(id: 1L))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role1", contest)).thenReturn(new EventRole(id: 1L, name: "Role1"))
        Mockito.when(eventRoleRepository.findByNameAndContest("Role2", contest)).thenReturn(null)

        shouldFail(ValidationException.class) {
            scheduleMngmService.parseEventImportFile(file, contest)
        }

        Mockito.verify(eventRepository, Mockito.times(0)).save(Matchers.any(Event.class))
    }

    def fileContent = "Event 1,event1,Event1,Event1,6/21/14 19:00,6/21/14 21:00,1,location1,Role1;Role2,Description,Thing to bring"

    void testParseEventImportFileNullFile() {
        def contest = new Contest(id: 1L)

        scheduleMngmService.parseEventImportFile(null, contest)

        Mockito.verifyZeroInteractions(eventRepository)
    }

    void testParseEventImportFileNullContest() {
        def fileContent = ""
        def file = new MockMultipartFile("events.csv", fileContent.getBytes(FormatUtils.DEFAULT_ENCODING))

        scheduleMngmService.parseEventImportFile(file, null)

        Mockito.verifyZeroInteractions(eventRepository)
    }


}
