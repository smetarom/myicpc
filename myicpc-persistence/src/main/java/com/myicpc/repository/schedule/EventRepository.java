package com.myicpc.repository.schedule;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Event;
import com.myicpc.model.schedule.EventRole;
import com.myicpc.model.schedule.Location;
import com.myicpc.model.schedule.ScheduleDay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * DAO repository for {@link Event}
 *
 * @author Roman Smetana
 */
public interface EventRepository extends CrudRepository<Event, Long>, EventDao {

    List<Event> findByContest(Contest contest);

    /**
     * Finds events for {@code contest} and load eagerly events {@link Location} and {@link ScheduleDay}
     *
     * @param contest contest
     * @return events for a contest
     */
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.scheduleDay LEFT JOIN FETCH e.location WHERE e.contest = ?1")
    List<Event> findByContestWithScheduleDayAndLocation(Contest contest);

    /**
     * Finds events for {@code location} and load eagerly events {@link Location} and {@link ScheduleDay}
     *
     * @param location event location
     * @return events in a location
     */
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.scheduleDay LEFT JOIN FETCH e.location WHERE e.location = ?1")
    List<Event> findByLocationWithScheduleDayAndLocation(Location location);

    List<Event> findByContestOrderByStartDateAsc(Contest contest);

    /**
     * Finds all events which ends in the future
     *
     * @param date    current date
     * @param contest contest
     * @return future events
     */
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.roles WHERE e.endDate > ?1 AND e.contest = ?2")
    List<Event> findAllFutureEvents(Date date, Contest contest);

    /**
     * Finds events, which end after {@code fromDate} and start before {@code toDate}
     * <p/>
     * It returns {@link Event} only with ID, name, startDate, endDate, and contest field
     * as a performance optimization
     *
     * @param fromDate start of interval
     * @param toDate   end of interval
     * @param contest  contest
     * @return events in the date interval
     */
    @Query("SELECT new com.myicpc.model.schedule.Event(e.id, e.name, e.startDate, e.endDate, e.contest) " +
            "FROM Event e " +
            "WHERE e.endDate >= ?1 AND e.startDate <= ?2 AND e.contest = ?3 " +
            "ORDER BY e.startDate")
    List<Event> findTimelineUpcomingEvents(Date fromDate, Date toDate, Contest contest);

    /**
     * Finds events which start between {@code startDate} and {@code endDate}
     *
     * @param startDate start of interval
     * @param endDate   end of interval
     * @param contest   contest
     * @return events starting in the date range
     */
    @Query("SELECT e FROM Event e WHERE e.contest = ?3 AND e.startDate BETWEEN ?1 AND ?2 ORDER BY e.startDate")
    List<Event> findAllBetweenDates(Date startDate, Date endDate, Contest contest);

    Long countByScheduleDay(ScheduleDay scheduleDay);

    Long countByLocation(Location location);

    /**
     * Counts number of events, which have a {@code eventRole} assigned
     *
     * @param eventRole event role
     * @return number of events with {@code eventRole}
     */
    @Query("SELECT COUNT(e) FROM Event e JOIN e.roles er WHERE er = ?1")
    Long countByEventRole(EventRole eventRole);

    Event findByCodeAndContest(String code, Contest contest);

    /**
     * Finds all not already published events which have already started
     *
     * @param date    current date
     * @param contest contest
     * @return
     */
    @Query("SELECT e FROM Event e WHERE e.published = false AND e.startDate < ?1 AND e.contest = ?2")
    List<Event> findNonpublishedOpenEvents(Date date, Contest contest);
}
