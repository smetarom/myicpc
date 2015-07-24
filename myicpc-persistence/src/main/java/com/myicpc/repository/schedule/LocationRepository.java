package com.myicpc.repository.schedule;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.Location;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * DAO repository for {@link Location}
 *
 * @author Roman Smetana
 */
public interface LocationRepository extends CrudRepository<Location, Long> {
    List<Location> findByContestOrderByNameAsc(Contest contest);

    Location findByCodeAndContest(String code, Contest contest);
}
