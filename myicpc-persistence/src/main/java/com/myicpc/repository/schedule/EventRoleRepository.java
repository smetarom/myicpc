package com.myicpc.repository.schedule;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.EventRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * DAO repository for {@link EventRole}
 *
 * @author Roman Smetana
 */
public interface EventRoleRepository extends CrudRepository<EventRole, Long> {
    List<EventRole> findByContest(Contest contest);

    List<EventRole> findByContestOrderByNameAsc(Contest contest);

    EventRole findByNameAndContest(String name, Contest contest);
}
