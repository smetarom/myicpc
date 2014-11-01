package com.myicpc.repository.schedule;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.schedule.ScheduleDay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleDayRepository extends CrudRepository<ScheduleDay, Long> {
    List<ScheduleDay> findByContestOrderByDateAsc(Contest contest);

    long countByContest(Contest contest);

    // ---

    ScheduleDay findByDayOrder(int dayOrder);

    @Query("SELECT sd FROM ScheduleDay sd ORDER BY sd.date ASC")
    List<ScheduleDay> findAllOrderByDateAsc();
}
