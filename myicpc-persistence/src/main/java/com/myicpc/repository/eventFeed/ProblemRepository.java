package com.myicpc.repository.eventFeed;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Problem findByCodeAndContest(String code, Contest contest);

    Problem findBySystemIdAndContest(Long systemId, Contest contest);

    List<Problem> findByContestOrderByCodeAsc(Contest contest);

    Long countByContest(Contest contest);

    // ----

    @Query("SELECT p FROM Problem p ORDER BY p.code")
    Iterable<Problem> findAllOrderByCode();

    Problem findByCode(String code);

    Problem findByCodeIgnoreCase(String code);

    /**
     * load all judgments and number of submissions for judgment if there is at
     * least one submission per judgment
     */
    @Query(value = "SELECT NEW org.apache.commons.lang3.tuple.ImmutablePair(tp.resultCode, COUNT(tp)) FROM TeamProblem tp WHERE tp.problem = ?1 AND tp.judged = true GROUP BY tp.resultCode HAVING COUNT(tp) > 0")
    List<ImmutablePair<String, Long>> getProblemReport(Problem problem);

    @Transactional
    @Modifying
    @Query("DELETE FROM Problem p WHERE p.contest = ?1")
    void deleteByContest(Contest contest);
}
