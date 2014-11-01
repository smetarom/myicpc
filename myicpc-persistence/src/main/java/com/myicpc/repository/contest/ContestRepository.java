package com.myicpc.repository.contest;

import com.myicpc.model.contest.Contest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    Contest findByName(String name);

    Contest findByCode(String code);

    @Query(value = "SELECT c FROM Contest c")
    Contest getContest();

    @Cacheable(value = "contestTime")
    @Query(value = "SELECT c.startTime FROM Contest c")
    Date getContestStartDate();

}
