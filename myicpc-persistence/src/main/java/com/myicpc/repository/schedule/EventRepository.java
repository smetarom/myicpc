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

public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findByContest(Contest contest);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.scheduleDay LEFT JOIN FETCH e.location WHERE e.contest = ?1")
    List<Event> findByContestWithScheduleDayAndLocation(Contest contest);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.scheduleDay LEFT JOIN FETCH e.location WHERE e.location = ?1")
    List<Event> findByLocationWithScheduleDayAndLocation(Location location);

    List<Event> findByContestOrderByStartDateAsc(Contest contest);

    @Query("SELECT e FROM Event e WHERE e.endDate > ?1 AND e.contest = ?2")
    List<Event> findAllFutureEvents(Date now, Contest contest);


    /**
     * @return Events starting between these two dates
     */
    @Query("SELECT e FROM Event e WHERE e.contest = ?3 AND e.startDate BETWEEN ?1 AND ?2 ORDER BY e.startDate")
    List<Event> findAllBetweenDates(Date startDate, Date endDate, Contest contest);

    // ---

    List<Event> findByLocation(Location location);

    Long countByScheduleDay(ScheduleDay scheduleDay);

    Long countByLocation(Location location);

    @Query("SELECT COUNT(e) FROM Event e JOIN e.roles er WHERE er = ?1")
    Long countByEventRole(EventRole eventRole);

    @Query("SELECT e FROM Event e ORDER BY e.startDate")
    List<Event> findAllOrderByStartDateAsc();

    @Query("SELECT e FROM Event e WHERE e.endDate > ?1")
    List<Event> findAllFutureEvents(Date now);

    Event findByHashtag(String hashtag);

    Event findByCode(String code);

    /**
     * @return All not already published events which has already started
     */
    @Query("SELECT e FROM Event e WHERE e.published = false AND e.startDate < ?1")
    List<Event> findNonpublishedOpenEvents(Date date);
}
