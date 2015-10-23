package com.myicpc.repository.schedule;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Event;
import com.myicpc.model.schedule.EventRole;
import com.myicpc.repository.AbstractDao;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Implementation of custom DAO methods from {@link EventDao}
 * and include them to {@link EventRepository}
 *
 * @author Roman Smetana
 */
public class EventRepositoryImpl extends AbstractDao implements EventDao {

    /**
     * Return all events for given roles
     *
     * @param roleIds
     *            selected roles
     * @return events for given roles
     */
    public List<Event> findEventsForRoles(final String[] roleIds, Contest contest) {
        return findEventsForRoles(roleIds, null, contest);
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
    public List<Event> findEventsForRoles(final String[] roleIds, final Date now, Contest contest) {
        return findEventsForRoles(roleIds, now, null, contest, false);
    }

    /**
     * Returns all events for given roles and which ends after the given date
     *
     * @param roleIds ids of selected roles
     * @param now     events must end after this date
     * @param limit   events must start before this date
     * @param contest contest
     * @param sorted  if sorted by start date
     * @return events for roles, in the given time range
     */
    public List<Event> findEventsForRoles(final String[] roleIds, final Date now, final Date limit, Contest contest, final boolean sorted) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Event> q = cb.createQuery(Event.class);
        Root<Event> c = q.from(Event.class);
        Join<Event, EventRole> eventRole = c.join("roles", JoinType.LEFT);
        q.select(c).distinct(true);
        Predicate rolesPredicate = cb.isEmpty(c.<List>get("roles"));
        if (roleIds != null) {
            for (String roleId : roleIds) {
                if (NumberUtils.isNumber(roleId)) {
                    rolesPredicate = cb.or(rolesPredicate, cb.equal(eventRole.<Long>get("id"), roleId));
                }
            }
        }

        Predicate datesPredicate = cb.conjunction();
        if (now != null) {
            datesPredicate = cb.and(datesPredicate, cb.greaterThanOrEqualTo(c.<Date>get("endDate"), now));
        }
        if (limit != null) {
            datesPredicate = cb.and(datesPredicate, cb.lessThanOrEqualTo(c.<Date>get("startDate"), limit));
        }
        q.where(cb.and(datesPredicate, rolesPredicate, cb.equal(c.<Contest>get("contest"), contest)));
        if (sorted) {
            q.orderBy(cb.asc(c.<Date>get("startDate")));
        }
        return getEntityManager().createQuery(q).getResultList();
    }
}
