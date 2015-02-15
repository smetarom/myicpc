package com.myicpc.service.schedule;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Lists;
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
import com.myicpc.service.EntityManagerService;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.validation.EventRoleValidator;
import com.myicpc.service.validation.LocationValidator;
import com.myicpc.service.validation.ScheduleDayValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class ScheduleMngmService {

    @Autowired
    private EventRepository eventRepository;

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



    public void saveEvent(final Event event) {
        eventRepository.save(event);

        // TODO generate schedule notification
//        List<Notification> notifications = notificationRepository.findByEntityIdAndNotificationType(event.getId(),
//                Notification.NotificationType.SCHEDULE_EVENT_OPEN);
//        for (Notification notification : notifications) {
//            notification.setTitle(event.getName());
//            notification.setBody(FormatUtils.formatEvent(event));
//        }
//        notificationRepository.save(notifications);
    }

    public void deleteEvent(final Event event) {
        notificationRepository.deleteByEntityIdAndNotificationType(event.getId(), NotificationType.SCHEDULE_EVENT_OPEN);
        eventRepository.delete(event);
    }

    public void saveLocation(Location location) throws BusinessValidationException {
        locationValidator.validate(location);
        locationRepository.save(location);
    }

    public void deleteLocation(final Location location) throws BusinessValidationException {
        Long count = eventRepository.countByLocation(location);
        if (count > 0) {
            throw new BusinessValidationException("scheduleAdmin.deleteLocation.failed.inUse", count, location.getName());
        }
        locationRepository.delete(location);
    }

    public void saveScheduleDay(ScheduleDay scheduleDay) throws BusinessValidationException {
        scheduleDayValidator.validate(scheduleDay);
        scheduleDayRepository.save(scheduleDay);
    }

    public void deleteScheduleDay(final ScheduleDay scheduleDay) throws BusinessValidationException {
        Long count = eventRepository.countByScheduleDay(scheduleDay);
        if (count > 0) {
            throw new BusinessValidationException("scheduleAdmin.deleteScheduleDay.failed.inUse", count, scheduleDay.getName());
        }
        scheduleDayRepository.delete(scheduleDay);
    }

    public void saveEventRole(EventRole eventRole) throws BusinessValidationException {
        eventRoleValidator.validate(eventRole);
        eventRoleRepository.save(eventRole);
    }

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
     * @see <a
     *      href="http://icpc.baylor.edu/xwiki/wiki/icpcdev/view/MyICPC/Administrator+guide#HImportschedule">http://icpc.baylor.edu/xwiki/wiki/icpcdev/view/MyICPC/Administrator+guide#HImportschedule</a>
     *
     *
     * @param rolesFile
     *            CSV with schedule roles
     * @param daysFile
     *            CSV with schedule days
     * @param locationsFile
     *            CSV with locations
     * @param eventFile
     *            CSV with events
     * @param contest
     * @throws IOException
     *             IO file read exception
     * @throws ParseException
     *             CSV parsing exception
     */
    @Transactional
    public void importSchedule(final MultipartFile rolesFile, final MultipartFile daysFile, final MultipartFile locationsFile,
                               final MultipartFile eventFile, final Contest contest) throws IOException, ParseException {
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
     * @see <a
     *      href="http://icpc.baylor.edu/xwiki/wiki/icpcdev/view/MyICPC/Administrator+guide#HImportschedule">http://icpc.baylor.edu/xwiki/wiki/icpcdev/view/MyICPC/Administrator+guide#HImportschedule</a>
     *
     * @param rolesFile
     *            event role file
     * @param contest
     * @throws IOException
     * @throws ParseException
     */
    public void parseScheduleRoleImportFile(final MultipartFile rolesFile, final Contest contest) throws IOException, ParseException {
        String[] line;
        try (InputStream in = rolesFile.getInputStream();
             CSVReader rolesReader = new CSVReader(new InputStreamReader(in, FormatUtils.DEFAULT_ENCODING))) {
            while ((line = rolesReader.readNext()) != null) {
                String[] ss = line;
                EventRole role = eventRoleRepository.findByName(ss[0]);
                if (role == null) {
                    role = new EventRole();
                }
                role.setContest(contest);
                role.setName(ss[0]);
                eventRoleRepository.save(role);
            }
        }
    }

    /**
     * Parses schedule days from import file
     *
     * @see <a
     *      href="http://icpc.baylor.edu/xwiki/wiki/icpcdev/view/MyICPC/Administrator+guide#HImportschedule">http://icpc.baylor.edu/xwiki/wiki/icpcdev/view/MyICPC/Administrator+guide#HImportschedule</a>
     *
     * @param daysFile
     *            schedule days file
     * @param contest
     * @throws IOException
     * @throws ParseException
     */
    public void parseScheduleDayImportFile(final MultipartFile daysFile, final Contest contest) throws IOException, ParseException {
        SimpleDateFormat date = MyICPCConstants.EXCEL_DATE_FORMAT;
        date.setLenient(true);
        String[] line;
        try (InputStream in = daysFile.getInputStream();
             CSVReader daysReader = new CSVReader(new InputStreamReader(in, FormatUtils.DEFAULT_ENCODING))) {
            while ((line = daysReader.readNext()) != null) {
                String[] ss = line;
                Integer order;
                try {
                    order = Integer.parseInt(ss[0]);
                } catch (NumberFormatException ex) {
                    throw new ValidationException("Day order " + ss[0] + " must be number.");
                }
                ScheduleDay day = scheduleDayRepository.findByDayOrder(order);
                if (day == null) {
                    day = new ScheduleDay();
                }
                day.setContest(contest);
                day.setDayOrder(order);
                day.setName(ss[1]);
                try {
                    day.setDate(processImportedDate(date.parse(ss[2]), contest));
                } catch (ParseException ex) {
                    throw new ValidationException("Wrong day date '" + ss[2] + "' - required format M/dd/yy");
                }
                if (ss.length > 3 && !StringUtils.isEmpty(ss[3])) {
                    day.setDescription(ss[3]);
                }
                scheduleDayRepository.save(day);
            }
        }
    }

    /**
     * Parses schedule locations from import file
     *
     * @see <a
     *      href="http://icpc.baylor.edu/xwiki/wiki/icpcdev/view/MyICPC/Administrator+guide#HImportschedule">http://icpc.baylor.edu/xwiki/wiki/icpcdev/view/MyICPC/Administrator+guide#HImportschedule</a>
     *
     * @param locationsFile
     *            location file
     * @param contest
     * @throws IOException
     * @throws ParseException
     */
    public void parseLocationImportFile(final MultipartFile locationsFile, final Contest contest) throws IOException, ParseException {
        try (InputStream in = locationsFile.getInputStream();
             CSVReader locationsReader = new CSVReader(new InputStreamReader(in, FormatUtils.DEFAULT_ENCODING))) {
            String[] line;
            while ((line = locationsReader.readNext()) != null) {
                String[] ss = line;
                Location location = locationRepository.findByCode(ss[1]);
                if (location == null) {
                    location = new Location();
                }
                location.setContest(contest);
                location.setName(ss[0]);
                location.setCode(ss[1]);
                if (ss.length > 2 && !StringUtils.isEmpty(ss[2])) {
                    location.setGoogleMapUrl(ss[2]);
                }
                locationRepository.save(location);
            }
        }
    }

    /**
     * Parses schedule events from import file
     *
     * @param contest
     *
     * @see <a
     *      href="http://icpc.baylor.edu/xwiki/wiki/icpcdev/view/MyICPC/Administrator+guide#HImportschedule">http://icpc.baylor.edu/xwiki/wiki/icpcdev/view/MyICPC/Administrator+guide#HImportschedule</a>
     *
     * @param eventFile
     *            event file
     * @throws IOException
     * @throws ParseException
     */
    public void parseEventImportFile(final MultipartFile eventFile, final Contest contest) throws IOException, ParseException {
        try (InputStream in = eventFile.getInputStream();
             CSVReader eventReader = new CSVReader(new InputStreamReader(in, FormatUtils.DEFAULT_ENCODING))) {
            SimpleDateFormat time = MyICPCConstants.EXCEL_DATE_TIME_FORMAT;
            time.setLenient(true);
            String[] line;
            // Iterates through the lines of CSV file, where each line
            // represents an event
            while ((line = eventReader.readNext()) != null) {
                String[] ss = line;
                Event event = eventRepository.findByCode(ss[1]);
                if (event == null) {
                    event = new Event();
                }
                event.setContest(contest);
                int i = 0;
                // Event name
                event.setName(ss[i++]);
                // Event code
                event.setCode(ss[i++]);
                // Event social network hashtag
                event.setHashtag(ss[i++]);
                // Event Picasa hashtag, if not empty
                if (!StringUtils.isEmpty(ss[i])) {
                    event.setPicasaTag(ss[i]);
                }
                i++;
                try {
                    // Check start and end date format and if current set them
                    // to the event
                    event.setStartDate(processImportedDate(time.parse(ss[i++]), contest));
                    event.setEndDate(processImportedDate(time.parse(ss[i++]), contest));
                } catch (ParseException ex) {
                    throw new ValidationException("Wrong '" + event.getName() + "' event date '" + ss[i - 1] + "' - required format M/dd/yy HH:mm");
                }
                try {
                    // Finds a schedule day and set it to the event, If provided
                    // schedule day order number is not valid, throw exception
                    Integer order = Integer.parseInt(ss[i++]);
                    ScheduleDay day = scheduleDayRepository.findByDayOrder(order);
                    if (day == null) {
                        throw new ValidationException("Day does not exist for '" + event.getName() + "' event.");
                    }
                    event.setScheduleDay(scheduleDayRepository.findByDayOrder(order));
                    if (ss.length > i && !StringUtils.isEmpty(ss[i])) {
                        event.setLocation(locationRepository.findByCode(ss[i]));
                    }
                } catch (NumberFormatException ex) {
                    throw new ValidationException("Day order for '" + event.getName() + "' event order must be number.");
                }
                i++;
                if (ss.length > i && !StringUtils.isEmpty(ss[i])) {
                    // Parse event roles and check if all of them are valid
                    Set<EventRole> roles = event.getRoles();
                    for (String s : ss[i].split(";")) {
                        s = s.trim();
                        if (!s.isEmpty()) {
                            EventRole role = eventRoleRepository.findByName(s);
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
                if (ss.length > i && !StringUtils.isEmpty(ss[i])) {
                    event.setDescription(ss[i]);
                }
                i++;
                // Event things to bring if not empty
                if (ss.length > i && !StringUtils.isEmpty(ss[i])) {
                    event.setThingsToBring(ss[i++]);
                }
                i++;
                eventRepository.save(event);
            }
        }
    }

    /**
     * Processes a given date to date compatible with database
     *
     * @param date
     *            date to be processed
     * @return date compatible with dates in database
     */
    private Date processImportedDate(final Date date, final Contest contest) {
        return TimeUtils.convertLocalDateToUTC(date, contest.getContestSettings().getTimeDifference());
    }
}
