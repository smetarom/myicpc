package com.myicpc.repository.schedule;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocationRepository extends CrudRepository<Location, Long> {
    List<Location> findByContestOrderByNameAsc(Contest contest);

    // ----
    @Query("SELECT l FROM Location l ORDER BY l.name")
    List<Location> findAllOrderByNameAsc();

    Location findByCode(String code);
}
