package com.myicpc.service.schedule;

import com.google.common.base.Joiner;
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
import com.myicpc.service.EntityManagerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
 * @author Roman Smetana
 */
@Service
@Transactional
public class ScheduleService extends EntityManagerService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRoleRepository eventRoleRepository;

    public Event getEventByIdOrCode(String eventId) {
        Event event;
        try {
            Long id = Long.parseLong(eventId);
            event = eventRepository.findOne(id);
        } catch (Exception ex) {
            event = eventRepository.findByCode(eventId);
        }
        event.getRoles().size();
        return event;
    }

    /**
     * Returns all events which ends after the given date
     *
     * @param today
     *            deadline date
     * @return events which ends after the given date
     */
    public Iterable<ScheduleDay> getCurrentContestSchedule(final Date today, final Contest contest) {
        Iterable<Event> events = eventRepository.findAllFutureEvents(today, contest);
        return getEventsGroupedByScheduleDay(events);
    }

    /**
     * Returns all events for given roles and which ends after the given date
     *
     * @param roleIds
     *            ids of selected roles
     * @param today
     *            deadline date
     * @param contest
     * @return events for given roles and which ends after the given date
     */
    public Iterable<ScheduleDay> getMyCurrentSchedule(final String[] roleIds, final Date today, Contest contest) {
        Iterable<Event> events = getEventsForRoles(roleIds, today, contest);
        return getEventsGroupedByScheduleDay(events);
    }

    /**
     * Returns complete schedule. Events are grouped by schedule day
     *
     * @return complete schedule
     */
    public Iterable<ScheduleDay> getEntireContestSchedule(final Contest contest) {
        Iterable<Event> events = eventRepository.findByContestWithScheduleDayAndLocation(contest);
        return getEventsGroupedByScheduleDay(events);
    }

    /**
     * Returns all events, which take place in the given {@link com.myicpc.model.schedule.Location}
     *
     * @param location
     *            location for events
     * @return events in the location
     */
    public Iterable<ScheduleDay> getScheduleEventsInLocation(final Location location) {
        Iterable<Event> events = eventRepository.findByLocationWithScheduleDayAndLocation(location);
        return getEventsGroupedByScheduleDay(events);
    }

    /**
     * Return all events for given roles
     *
     * @param roleIds
     *            selected roles
     * @return events for given roles
     */
    public List<Event> getEventsForRoles(final String[] roleIds, Contest contest) {
        return getEventsForRoles(roleIds, null, contest);
    }

    /**
     * Returns all events for given roles and which ends after the given date
     *
     * @param roleIds
     *            ids of selected roles
     * @param now
     *            deadline date
     * @param contest
     * @return events for given roles and which ends after the given date
     */
    public List<Event> getEventsForRoles(final String[] roleIds, final Date now, Contest contest) {
        return getEventsForRoles(roleIds, now, null, contest, false);
    }

    /**
     * Returns all events for given roles and which ends after the given date
     *
     * @param roleIds
     *            ids of selected roles
     * @param now
     *            events must end after this date
     * @param limit
     *            events must start before this date
     * @param contest
     *@param sorted
     *            if sorted by start date  @return events for roles, in the given time range
     */
    public List<Event> getEventsForRoles(final String[] roleIds, final Date now, final Date limit, Contest contest, final boolean sorted) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> q = cb.createQuery(Event.class);
        Root<Event> c = q.from(Event.class);
        Join<Event, EventRole> eventRole = c.join("roles");
        q.select(c).distinct(true);
        Predicate rolesPredicate = cb.disjunction(); // cb.isNull(c.<String>
        // get("roles"));
        for (String roleId : roleIds) {
            rolesPredicate = cb.or(rolesPredicate, cb.equal(eventRole.<Long> get("id"), roleId));
        }

        Predicate datesPredicate = cb.conjunction();
        if (now != null) {
            datesPredicate = cb.and(datesPredicate, cb.greaterThanOrEqualTo(c.<Date> get("endDate"), now));
        }
        if (limit != null) {
            datesPredicate = cb.and(datesPredicate, cb.lessThanOrEqualTo(c.<Date> get("startDate"), limit));
        }
        q.where(cb.and(datesPredicate, rolesPredicate, cb.equal(c.<Contest> get("contest"), contest)));
        if (sorted) {
            q.orderBy(cb.asc(c.<Date> get("startDate")));
        }
        return em.createQuery(q).getResultList();
    }

    /**
     * Groups a list of events by {@link ScheduleDay}
     *
     * @param events
     *            events to be grouped
     * @return schedule days with events
     */
    private Iterable<ScheduleDay> getEventsGroupedByScheduleDay(final Iterable<Event> events) {
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
    public String getAllScheduleRoles(final Contest contest) {
        List<EventRole> roles = eventRoleRepository.findByContest(contest);
        if (roles == null || roles.isEmpty()) {
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
        Map<Long, Boolean> activeRoles = new HashMap<Long, Boolean>();

        if (!StringUtils.isEmpty(scheduleRoles)) {
            String[] ss = scheduleRoles.split(",");
            for (int i = 0; i < ss.length; i++) {
                activeRoles.put(Long.parseLong(ss[i]), true);
            }
        }

        return activeRoles;
    }

    /**
     * Events which start after startDate and end before endDate
     *
     * @param startDate
     *            start date of period
     * @param endDate
     *            end date of period
     * @return events in the period
     */
    public List<Event> getEventsInPeriod(final Date startDate, final Date endDate, final Contest contest) {
        return eventRepository.findAllBetweenDates(startDate, endDate, contest);
    }

    public List<Event> getUpcomingEventsForUser(String scheduleRoles, int hours, final Contest contest) {
        Date now = new GregorianCalendar(2014, 5, 22, 12, 0, 0).getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        if (!StringUtils.isEmpty(scheduleRoles)) {
            String[] splitRoles = scheduleRoles.split(",");
            return getEventsForRoles(splitRoles, now, cal.getTime(), contest, true);
        } else {
            return getEventsInPeriod(now, cal.getTime(), contest);
        }
    }

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
