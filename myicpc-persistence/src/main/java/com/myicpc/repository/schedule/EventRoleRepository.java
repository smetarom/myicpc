package com.myicpc.repository.schedule;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.EventRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRoleRepository extends CrudRepository<EventRole, Long> {
    List<EventRole> findByContest(Contest contest);

    List<EventRole> findByContestOrderByNameAsc(Contest contest);

    // ----

    @Query("SELECT er FROM EventRole er ORDER BY er.name ASC")
    List<EventRole> findAllOrderedByNameAsc();

    EventRole findByName(String name);
}
