package com.myicpc.repository.poll;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.poll.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByContestOrderByStartDateDesc(Contest contest);

    @Query(value = "SELECT p FROM Poll p WHERE p.contest = ?1 AND p.startDate < ?2 ORDER BY p.startDate DESC")
    List<Poll> findOpenPolls(Contest contest, Date date);

    @Query(value = "SELECT p FROM Poll p WHERE p.published = false AND p.contest = ?2 AND p.startDate < ?1")
    List<Poll> findAllNonpublishedStartedPolls(Date date, Contest contest);

    // ----


    @Query(value = "SELECT p FROM Poll p WHERE p.opened = true AND (p.endDate < ?1 OR p.correctAnswer IS NULL OR p.conclusionMessage IS NULL)")
    List<Poll> findAllNonpublishedClosedPolls(Date date);

    @Query(value = "SELECT p FROM Poll p WHERE ?1 BETWEEN p.startDate AND p.endDate AND (p.correctAnswer IS NULL OR p.conclusionMessage IS NULL)")
    List<Poll> findActivePollsAtTime(Date date);

    @Query(value = "SELECT p FROM Poll p WHERE ?1 BETWEEN p.startDate AND p.endDate")
    List<Poll> findActivePolls(Date date);

    List<Poll> findByStartDateLessThanOrderByStartDateDesc(Date date);
}
