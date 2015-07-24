package com.myicpc.repository.schedule;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Event;

import java.util.Date;
import java.util.List;

/**
 * Custom DAO methods for {@link EventRepository}
 *
 * @author Roman Smetana
 */
public interface EventDao {
    List<Event> findEventsForRoles(final String[] roleIds, Contest contest);

    List<Event> findEventsForRoles(final String[] roleIds, final Date now, Contest contest);

    List<Event> findEventsForRoles(final String[] roleIds, final Date now, final Date limit, Contest contest, final boolean sorted);
}
