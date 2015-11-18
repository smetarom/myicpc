package com.myicpc.repository.contest;

import com.myicpc.model.contest.Contest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    Contest findByName(String name);

    Contest findByCode(String code);

    List<Contest> findByStartTimeGreaterThanEqual(Date date, Sort sort);

    @Query("FROM Contest c " +
            "JOIN FETCH c.contestSettings " +
            "JOIN FETCH c.webServiceSettings " +
            "JOIN FETCH c.questConfiguration " +
            "JOIN FETCH c.moduleConfiguration " +
            "JOIN FETCH c.mapConfiguration " +
            "WHERE c.code = ?1")
    Contest findFullByCode(String code);

    List<Contest> findByHidden(boolean hidden, Sort sort);
}
