package com.myicpc.service.schedule;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gson.JsonObject;
import com.myicpc.commons.MyICPCConstants;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.commons.utils.TimeUtils;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Event;
import com.myicpc.model.schedule.EventRole;
import com.myicpc.model.schedule.Location;
import com.myicpc.model.schedule.ScheduleDay;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.schedule.EventRepository;
import com.myicpc.repository.schedule.EventRoleRepository;
import com.myicpc.repository.schedule.LocationRepository;
import com.myicpc.repository.schedule.ScheduleDayRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.notification.NotificationBuilder;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.publish.PublishService;
import com.myicpc.service.schedule.dto.EditScheduleDTO;
import com.myicpc.service.validation.EventRoleValidator;
import com.myicpc.service.validation.EventValidator;
import com.myicpc.service.validation.LocationValidator;
import com.myicpc.service.validation.ScheduleDayValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Service which is responsible for CRUD management of schedule
 * <p/>
 * Class has CRUD methods for schedule entities - {@link Event}, {@link EventRole}, {@link Location}, {@link ScheduleDay}
 *
 * @author Roman Smetana
 */
@Service
@Transactional
public class ScheduleMngmService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleMngmService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventValidator eventValidator;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationValidator locationValidator;

    @Autowired
    private ScheduleDayRepository scheduleDayRepository;

    @Autowired
    private ScheduleDayValidator scheduleDayValidator;

    @Autowired
    private EventRoleRepository eventRoleRepository;

    @Autowired
    private EventRoleValidator eventRoleValidator;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PublishService publishService;

    @Autowired
    private NotificationService notificationService;

    /**
     * Saves the updates of {@code event}
     * <p/>
     * It propagates changes to all already existing event notifications if any
     *
     * @param event event to be updated
     * @return updated event
     */
    public Event saveEvent(final Event event) throws BusinessValidationException {
        eventValidator.validate(event);
        Event updatedEvent = eventRepository.save(event);

        notificationService.modifyExistingNotifications(updatedEvent, NotificationType.SCHEDULE_EVENT_OPEN, new NotificationService.NotificationModifier() {
            @Override
            public void modify(Notification notification) {
                notification.setTitle(event.getName());
                notification.setBody(ScheduleMngmService.createEventNotificationBody(event).toString());
            }
        });
        return updatedEvent;
    }

    /**
     * Deletes an {@code event}
     * <p/>
     * It deletes related notifications as well
     *
     * @param event event to be deleted
     */
    public void deleteEvent(final Event event) {
        notificationRepository.deleteByEntityIdAndNotificationType(event.getId(), NotificationType.SCHEDULE_EVENT_OPEN, event.getContest());
        eventRepository.delete(event);
    }

    /**
     * Creates a wrapper for a bulk schedule update
     *
     * @param contest contest
     * @return schedule edit wrapper
     */
    @Transactional(readOnly = true)
    public EditScheduleDTO createEditSchedule(final Contest contest) {
        List<Event> events = eventRepository.findByContestWithScheduleDayAndLocation(contest);
        // prevent lazy-loading exception
        for (Event event : events) {
            event.getRoles().size();
        }
        Collections.sort(events);
        return new EditScheduleDTO(events);
    }

    /**
     * Saves all events in the bulk update
     *
     * @param editScheduleDTO holder with updated events
     */
    @Transactional
    public void saveBulkEdit(final EditScheduleDTO editScheduleDTO) throws BusinessValidationException {
        if (editScheduleDTO == null || CollectionUtils.isEmpty(editScheduleDTO.getEvents())) {
            return;
        }
        for (Event event : editScheduleDTO.getEvents()) {
            saveEvent(event);
        }
    }

    /**
     * Saves {@code location} updates
     *
     * @param location location to be updated
     * @return updated location
     * @throws BusinessValidationException {@code location} contains invalid data
     */
    public Location saveLocation(Location location) throws BusinessValidationException {
        locationValidator.validate(location);
        return locationRepository.save(location);
    }

    /**
     * Deletes {@code location}
     *
     * @param location location to be deleted
     * @throws BusinessValidationException {@code location} is used in one or more events
     */
    public void deleteLocation(final Location location) throws BusinessValidationException {
        Long count = eventRepository.countByLocation(location);
        if (count > 0) {
            throw new BusinessValidationException("scheduleAdmin.deleteLocation.failed.inUse", count, location.getName());
        }
        locationRepository.delete(location);
    }

    /**
     * Saves {@code scheduleDay} updates
     *
     * @param scheduleDay schedule day to updated
     * @return updated schedule day
     * @throws BusinessValidationException {@code scheduleDay} contains invalid data
     */
    public ScheduleDay saveScheduleDay(ScheduleDay scheduleDay) throws BusinessValidationException {
        scheduleDayValidator.validate(scheduleDay);
        return scheduleDayRepository.save(scheduleDay);
    }

    /**
     * Deletes {@code scheduleDay}
     *
     * @param scheduleDay schedule day to be deleted
     * @throws BusinessValidationException {@code scheduleDay} is used in one or more events
     */
    public void deleteScheduleDay(final ScheduleDay scheduleDay) throws BusinessValidationException {
        Long count = eventRepository.countByScheduleDay(scheduleDay);
        if (count > 0) {
            throw new BusinessValidationException("scheduleAdmin.deleteScheduleDay.failed.inUse", count, scheduleDay.getName());
        }
        scheduleDayRepository.delete(scheduleDay);
    }

    /**
     * Saves {@code eventRole} updates
     *
     * @param eventRole event role to be updated
     * @return updated event role
     * @throws BusinessValidationException {@code eventRole} contains invalid data
     */
    public EventRole saveEventRole(EventRole eventRole) throws BusinessValidationException {
        eventRoleValidator.validate(eventRole);
        return eventRoleRepository.save(eventRole);
    }

    /**
     * Creates {@link Notification}s for all non published {@link Event}s,
     * which are open now
     *
     * @param contest contest
     */
    @Transactional
    public void createNonPublishedEventNotifications(final Contest contest) {
        List<Event> nonpublishedEvents = eventRepository.findNonpublishedOpenEvents(new Date(), contest);
        for (Event event : nonpublishedEvents) {
            NotificationBuilder builder = new NotificationBuilder(event);
            builder.setTitle(event.getName());
            builder.setBody(ScheduleMngmService.createEventNotificationBody(event).toString());
            builder.setNotificationType(NotificationType.SCHEDULE_EVENT_OPEN);

            Notification notification = notificationRepository.save(builder.build());
            publishService.broadcastNotification(notification, contest);
            event.setPublished(true);
        }
        eventRepository.save(nonpublishedEvents);
    }

    /**
     * Deletes {@code eventRole}
     *
     * @param eventRole event role to be deleted
     * @throws BusinessValidationException {@code eventRole} is used in one or more events
     */
    public void deleteEventRole(final EventRole eventRole) throws BusinessValidationException {
        Long count = eventRepository.countByEventRole(eventRole);
        if (count > 0) {
            throw new BusinessValidationException("scheduleAdmin.deleteEventRole.failed.inUse", count, eventRole.getName());
        }
        eventRoleRepository.delete(eventRole);
    }

    /**
     * Takes CSV files and creates schedule items from them
     *
     * @param rolesFile     CSV with schedule roles
     * @param daysFile      CSV with schedule days
     * @param locationsFile CSV with locations
     * @param eventFile     CSV with events
     * @param contest
     * @throws IOException                 IO file read exception
     * @throws ParseException              CSV parsing exception
     * @throws BusinessValidationException imported data is not valid
     */
    @Transactional
    public void importSchedule(final MultipartFile rolesFile, final MultipartFile daysFile, final MultipartFile locationsFile,
                               final MultipartFile eventFile, final Contest contest) throws IOException, ParseException, BusinessValidationException {
        // process EventRoles
        parseScheduleRoleImportFile(rolesFile, contest);

        // process ScheduleDays
        parseScheduleDayImportFile(daysFile, contest);

        // process Locations
        parseLocationImportFile(locationsFile, contest);

        // process Events
        parseEventImportFile(eventFile, contest);
    }

    /**
     * Parses event roles from import file
     *
     * @param rolesFile event role file
     * @param contest   contest
     * @throws IOException                 error during reading the file
     * @throws BusinessValidationException role contains invalid data
     */
    @Transactional
    public void parseScheduleRoleImportFile(final MultipartFile rolesFile, final Contest contest) throws IOException, BusinessValidationException {
        if (rolesFile == null || contest == null) {
            logger.warn("Schedule roles export skipped. The file or contest is not defined.");
            return;
        }
        String[] line;
        try (InputStream in = rolesFile.getInputStream();
             CSVReader rolesReader = new CSVReader(new InputStreamReader(in, FormatUtils.DEFAULT_ENCODING))) {
            while ((line = rolesReader.readNext()) != null) {
                EventRole role = eventRoleRepository.findByNameAndContest(line[0], contest);
                if (role == null) {
                    role = new EventRole();
                }
                role.setContest(contest);
                role.setName(line[0]);
                eventRoleValidator.validate(role);
                eventRoleRepository.save(role);
            }
        }
    }

    /**
     * Parses schedule days from import file
     *
     * @param daysFile schedule days file
     * @param contest  contest
     * @throws IOException                 error during reading the file
     * @throws BusinessValidationException role contains invalid data
     */
    @Transactional
    public void parseScheduleDayImportFile(final MultipartFile daysFile, final Contest contest) throws IOException, BusinessValidationException {
        if (daysFile == null || contest == null) {
            logger.warn("Schedule roles export skipped. The file or contest is not defined.");
            return;
        }
        SimpleDateFormat date = MyICPCConstants.EXCEL_DATE_FORMAT;
        date.setLenient(true);
        String[] line;
        try (InputStream in = daysFile.getInputStream();
             CSVReader daysReader = new CSVReader(new InputStreamReader(in, FormatUtils.DEFAULT_ENCODING))) {
            while ((line = daysReader.readNext()) != null) {
                Integer order;
                try {
                    order = Integer.parseInt(line[0]);
                } catch (NumberFormatException ex) {
                    throw new ValidationException("Day order " + line[0] + " must be number.");
                }
                ScheduleDay day = scheduleDayRepository.findByDayOrderAndContest(order, contest);
                if (day == null) {
                    day = new ScheduleDay();
                }
                day.setContest(contest);
                day.setDayOrder(order);
                day.setName(line[1]);
                try {
                    day.setDate(processImportedDate(date.parse(line[2]), contest));
                } catch (ParseException ex) {
                    throw new ValidationException("Wrong day date '" + line[2] + "' - required format M/dd/yy");
                }
                if (line.length > 3 && !StringUtils.isEmpty(line[3])) {
                    day.setDescription(line[3]);
                }
                scheduleDayValidator.validate(day);
                scheduleDayRepository.save(day);
            }
        }
    }

    /**
     * Parses schedule locations from import file
     *
     * @param locationsFile location file
     * @param contest       contest
     * @throws IOException                 error during reading the file
     * @throws BusinessValidationException schedule dat contains invalid data
     */
    public void parseLocationImportFile(final MultipartFile locationsFile, final Contest contest) throws IOException, BusinessValidationException {
        if (locationsFile == null || contest == null) {
            logger.warn("Schedule roles export skipped. The file or contest is not defined.");
            return;
        }
        try (InputStream in = locationsFile.getInputStream();
             CSVReader locationsReader = new CSVReader(new InputStreamReader(in, FormatUtils.DEFAULT_ENCODING))) {
            String[] line;
            while ((line = locationsReader.readNext()) != null) {
                Location location = locationRepository.findByCodeAndContest(line[1], contest);
                if (location == null) {
                    location = new Location();
                }
                location.setContest(contest);
                location.setName(line[0]);
                location.setCode(line[1]);
                if (line.length > 2 && !StringUtils.isEmpty(line[2])) {
                    location.setGoogleMapUrl(line[2]);
                }
                locationValidator.validate(location);
                locationRepository.save(location);
            }
        }
    }

    /**
     * Parses schedule events from import file
     *
     * @param eventFile CSV file with data
     * @param contest   contest
     * @throws IOException                 error during reading the file
     * @throws BusinessValidationException event contains invalid data
     */
    public void parseEventImportFile(final MultipartFile eventFile, final Contest contest) throws IOException {
        if (eventFile == null || contest == null) {
            logger.warn("Schedule roles export skipped. The file or contest is not defined.");
            return;
        }
        try (InputStream in = eventFile.getInputStream();
             CSVReader eventReader = new CSVReader(new InputStreamReader(in, FormatUtils.DEFAULT_ENCODING))) {
            SimpleDateFormat time = MyICPCConstants.EXCEL_DATE_TIME_FORMAT;
            time.setLenient(true);
            String[] line;
            // Iterates through the lines of CSV file, where each line
            // represents an event
            while ((line = eventReader.readNext()) != null) {
                Event event = eventRepository.findByCodeAndContest(line[1], contest);
                if (event == null) {
                    event = new Event();
                }
                event.setContest(contest);
                int i = 0;
                // Event name
                event.setName(line[i++]);
                // Event code
                event.setCode(line[i++]);
                // Event social network hashtag
                event.setHashtag(line[i++]);
                // Event Picasa hashtag, if not empty
                if (!StringUtils.isEmpty(line[i])) {
                    event.setPicasaTag(line[i]);
                }
                i++;
                try {
                    // Check start and end date format and if current set them
                    // to the event
                    event.setStartDate(processImportedDate(time.parse(line[i++]), contest));
                    event.setEndDate(processImportedDate(time.parse(line[i++]), contest));
                } catch (ParseException ex) {
                    throw new ValidationException("Wrong '" + event.getName() + "' event date '" + line[i - 1] + "' - required format M/dd/yy HH:mm");
                }
                try {
                    // Finds a schedule day and set it to the event, If provided
                    // schedule day order number is not valid, throw exception
                    Integer order = Integer.parseInt(line[i]);
                    ScheduleDay day = scheduleDayRepository.findByDayOrderAndContest(order, contest);
                    if (day == null) {
                        throw new ValidationException("Day does not exist for '" + event.getName() + "' event.");
                    }
                    event.setScheduleDay(day);
                } catch (NumberFormatException ex) {
                    throw new ValidationException("Day order for '" + event.getName() + "' event order must be number.");
                }
                i++;
                if (line.length > i && !StringUtils.isEmpty(line[i])) {
                    event.setLocation(locationRepository.findByCodeAndContest(line[i], contest));
                }
                i++;
                if (line.length > i && !StringUtils.isEmpty(line[i])) {
                    // Parse event roles and check if all of them are valid
                    Set<EventRole> roles = event.getRoles();
                    for (String s : line[i].split(";")) {
                        s = s.trim();
                        if (!s.isEmpty()) {
                            EventRole role = eventRoleRepository.findByNameAndContest(s, contest);
                            if (role != null) {
                                roles.add(role);
                            } else {
                                throw new ValidationException("Role '" + s + "' does not exists for event '" + event.getName() + "'.");
                            }
                        }
                    }
                    event.setRoles(roles);
                }
                i++;
                // Event description if not empty
                if (line.length > i && !StringUtils.isEmpty(line[i])) {
                    event.setDescription(line[i]);
                }
                i++;
                // Event things to bring if not empty
                if (line.length > i && !StringUtils.isEmpty(line[i])) {
                    event.setThingsToBring(line[i++]);
                }
                i++;
                eventRepository.save(event);
            }
        }
    }

    /**
     * Processes a given date to date compatible with database
     *
     * @param date date to be processed
     * @return date compatible with dates in database
     */
    private Date processImportedDate(final Date date, final Contest contest) {
        return TimeUtils.convertLocalDateToUTC(date, contest.getTimeDifference());
    }

    /**
     * Creates {@link Notification#body} from {@link Event}
     *
     * @param event schedule event
     * @return JSON representation ev event notification
     */
    public static JsonObject createEventNotificationBody(final Event event) {
        JsonObject eventBody = new JsonObject();

        DateFormat formatter = new SimpleDateFormat("MMM d, HH:mm");
        eventBody.addProperty("startDate", formatter.format(event.getLocalStartDate()));
        boolean isSameDate = DateUtils.isSameDay(event.getStartDate(), event.getEndDate());
        eventBody.addProperty("endDate", isSameDate ? TimeUtils.getTimeFormat().format(event.getLocalEndDate()) : formatter.format(event.getLocalEndDate()));
        if (event.getLocation() != null) {
            eventBody.addProperty("location", event.getLocation().getName());
        }
        if (CollectionUtils.isNotEmpty(event.getRoles())) {
            eventBody.addProperty("roles", event.getRolesPrint());
        }
        if (StringUtils.isNotEmpty(event.getThingsToBring())) {
            eventBody.addProperty("thingsToBring", event.getThingsToBring());
        }

        return eventBody;
    }
}
