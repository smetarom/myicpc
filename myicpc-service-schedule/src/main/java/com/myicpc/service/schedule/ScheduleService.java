package com.myicpc.service.schedule;

import com.google.common.collect.Lists;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Event;
import com.myicpc.model.schedule.EventRole;
import com.myicpc.model.schedule.Location;
import com.myicpc.model.schedule.ScheduleDay;
import com.myicpc.repository.schedule.EventRepository;
import com.myicpc.service.EntityManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
     * @return events for given roles and which ends after the given date
     */
    public Iterable<ScheduleDay> getMyCurrentSchedule(final String[] roleIds, final Date today) {
        Iterable<Event> events = getEventsForRoles(roleIds, today);
        return getEventsGroupedByScheduleDay(events);
    }

    /**
     * Returns complete schedule. Events are grouped by schedule day
     *
     * @return complete schedule
     */
    public Iterable<ScheduleDay> getEntireContestSchedule(final Contest contest) {
        Iterable<Event> events = eventRepository.findByContest(contest);
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
        Iterable<Event> events = eventRepository.findByLocation(location);
        return getEventsGroupedByScheduleDay(events);
    }

    /**
     * Return all events for given roles
     *
     * @param roleIds
     *            selected roles
     * @return events for given roles
     */
    public List<Event> getEventsForRoles(final String[] roleIds) {
        return getEventsForRoles(roleIds, null);
    }

    /**
     * Returns all events for given roles and which ends after the given date
     *
     * @param roleIds
     *            ids of selected roles
     * @param now
     *            deadline date
     * @return events for given roles and which ends after the given date
     */
    public List<Event> getEventsForRoles(final String[] roleIds, final Date now) {
        return getEventsForRoles(roleIds, now, null, false);
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
     * @param sorted
     *            if sorted by start date
     * @return events for roles, in the given time range
     */
    public List<Event> getEventsForRoles(final String[] roleIds, final Date now, final Date limit, final boolean sorted) {
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
        q.where(cb.and(datesPredicate, rolesPredicate));
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
}
