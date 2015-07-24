package com.myicpc.repository.schedule;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.ScheduleDay;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * DAO repository for {@link ScheduleDay}
 *
 * @author Roman Smetana
 */
public interface ScheduleDayRepository extends CrudRepository<ScheduleDay, Long> {
    List<ScheduleDay> findByContestOrderByDateAsc(Contest contest);

    long countByContest(Contest contest);

    ScheduleDay findByDayOrderAndContest(int dayOrder, Contest contest);
}
