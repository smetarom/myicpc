package com.myicpc.service.schedule;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Event;
import com.myicpc.model.schedule.EventRole;
import com.myicpc.model.schedule.Location;
import com.myicpc.model.schedule.ScheduleDay;
import com.myicpc.repository.schedule.EventRepository;
import com.myicpc.repository.schedule.EventRoleRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service method for {@link Event} and related entities actions
 *
 * @author Roman Smetana
 */
@Service
public class ScheduleService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRoleRepository eventRoleRepository;

    /**
     * Finds an event by identifier
     * <p/>
     * Identifier can be event ID or event code
     *
     * @param eventId event ID or event code
     * @param contest contest
     * @return event matching the identifier
     */
    @Transactional(readOnly = true)
    public Event getEventByIdOrCode(String eventId, final Contest contest) {
        Event event;
        try {
            Long id = Long.parseLong(eventId);
            event = eventRepository.findOne(id);
        } catch (Exception ex) {
            event = eventRepository.findByCodeAndContest(eventId, contest);
        }
        if (event != null && event.getRoles() != null) {
            // fetch roles to prevent lazy-loading exception
            event.getRoles().size();
        }
        return event;
    }

    /**
     * Returns all events which ends after the given date
     *
     * @param today deadline date
     * @return events which ends after the given date
     */
    @Transactional(readOnly = true)
    public Iterable<ScheduleDay> getCurrentContestSchedule(final Date today, final Contest contest) {
        Iterable<Event> events = eventRepository.findAllFutureEvents(today, contest);
        return getEventsGroupedByScheduleDay(events);
    }

    /**
     * Returns all events for given roles and which ends after the given date
     *
     * @param roleIds ids of selected roles
     * @param today   deadline date
     * @param contest contest
     * @return events for given roles and which ends after the given date
     */
    @Transactional(readOnly = true)
    public Iterable<ScheduleDay> getMyCurrentSchedule(final String[] roleIds, final Date today, Contest contest) {
        Iterable<Event> events = eventRepository.findEventsForRoles(roleIds, today, contest);
        return getEventsGroupedByScheduleDay(events);
    }

    /**
     * Returns complete schedule. Events are grouped by schedule day
     *
     * @return complete schedule
     */
    @Transactional(readOnly = true)
    public Iterable<ScheduleDay> getEntireContestSchedule(final Contest contest) {
        Iterable<Event> events = eventRepository.findByContestWithScheduleDayAndLocation(contest);
        return getEventsGroupedByScheduleDay(events);
    }

    /**
     * Returns all events, which take place in the given {@link com.myicpc.model.schedule.Location}
     *
     * @param location location for events
     * @return events in the location
     */
    @Transactional(readOnly = true)
    public Iterable<ScheduleDay> getScheduleEventsInLocation(final Location location) {
        Iterable<Event> events = eventRepository.findByLocationWithScheduleDayAndLocation(location);
        return getEventsGroupedByScheduleDay(events);
    }

    /**
     * Groups a list of events by {@link ScheduleDay}
     *
     * @param events events to be grouped
     * @return schedule days with events
     */
    private Iterable<ScheduleDay> getEventsGroupedByScheduleDay(final Iterable<Event> events) {
        if (events == null) {
            return Collections.EMPTY_LIST;
        }

        Map<ScheduleDay, List<Event>> dayMap = new HashMap<>();

        for (Event event : events) {
            if (dayMap.containsKey(event.getScheduleDay())) {
                dayMap.get(event.getScheduleDay()).add(event);
            } else {
                dayMap.put(event.getScheduleDay(), Lists.newArrayList(event));
            }
        }

        List<ScheduleDay> days = new ArrayList<>();
        for (Map.Entry<ScheduleDay, List<Event>> entry : dayMap.entrySet()) {
            entry.getKey().setEventsChronologically(entry.getValue());
            days.add(entry.getKey());
        }

        Collections.sort(days);

        return days;
    }

    /**
     * Returns all schedule roles, separated by comma
     *
     * @return all schedule roles IDs, separated by comma
     */
    @Transactional(readOnly = true)
    public String getAllScheduleRoles(final Contest contest) {
        List<EventRole> roles = eventRoleRepository.findByContest(contest);
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (EventRole eventRole : roles) {
            sb.append(eventRole.getId()).append(',');
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Mapping between {@link com.myicpc.model.schedule.EventRole#id} and if the role is active for the current user
     *
     * @param scheduleRoles list of role ids separated by comma
     * @return
     */
    public Map<Long, Boolean> getActiveEventRoleMapping(String scheduleRoles) {
        Map<Long, Boolean> activeRoles = new HashMap<>();

        if (!StringUtils.isEmpty(scheduleRoles)) {
            String[] ss = scheduleRoles.split(",");
            for (int i = 0; i < ss.length; i++) {
                try {
                    activeRoles.put(Long.parseLong(ss[i]), true);
                } catch (NumberFormatException e) {
                    // ignore non number values
                }
            }
        }

        return activeRoles;
    }

    /**
     * Events, which are active in next {@code hours} from now
     *
     * @param hours   the length of time range
     * @param contest contest
     * @return events active in time range
     */
    public List<Event> getUpcomingEvents(int hours, final Contest contest) {
        // TODO replace with new Date()
        Date now = new GregorianCalendar(2014, 5, 22, 12, 0, 0).getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return eventRepository.findTimelineUpcomingEvents(now, cal.getTime(), contest);
    }

    /**
     * Creates a JSON representation of the schedule
     * <p/>
     * The structure is following:
     * - on the top level are {@link ScheduleDay}s
     * - under each @{link ScheduleDay} is a list of all {@link Event}s, which belong to the day
     *
     * @param now     current date
     * @param contest contest
     * @return JSON schedule representation
     */
    public JsonArray getScheduleDaysJSON(Date now, Contest contest) {
        Iterable<ScheduleDay> iterable = getCurrentContestSchedule(now, contest);
        JsonArray arr = new JsonArray();
        if (iterable != null) {
            for (ScheduleDay day : iterable) {
                arr.add(getDayInJson(day));
            }
        }
        return arr;
    }

    private JsonObject getDayInJson(final ScheduleDay day) {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        JsonObject notificationObject = new JsonObject();
        notificationObject.addProperty("id", day.getId());
        notificationObject.addProperty("localDate", formatter.format(day.getLocalDate()));
        notificationObject.addProperty("name", day.getName());
        notificationObject.add("events", getScheduleEventsJSON(day.getEventsChronologically()));
        notificationObject.addProperty("isToday", DateUtils.isSameDay(day.getDate(), new Date()));

        return notificationObject;
    }

    private JsonArray getScheduleEventsJSON(List<Event> iterable) {
        JsonArray arr = new JsonArray();
        if (iterable != null) {
            for (Event event : iterable) {
                arr.add(getEventInJson(event));
            }
        }
        return arr;
    }

    private JsonObject getEventInJson(final Event event) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");

        JsonObject notificationObject = new JsonObject();
        notificationObject.addProperty("id", event.getId());
        notificationObject.addProperty("localStartDate", formatter.format(event.getLocalStartDate()));
        notificationObject.addProperty("localEndDate", formatter.format(event.getLocalEndDate()));
        notificationObject.addProperty("name", event.getName());
        if (event.getLocation() != null) {
            notificationObject.addProperty("venue", event.getLocation().getName());
        }
        notificationObject.addProperty("roles", event.getRolesPrint());
        notificationObject.addProperty("code", event.getCode());

        return notificationObject;
    }
}
