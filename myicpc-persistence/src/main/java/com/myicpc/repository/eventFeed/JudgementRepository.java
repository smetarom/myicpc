package com.myicpc.repository.eventFeed;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Judgement;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JudgementRepository extends JpaRepository<Judgement, Long> {
    Judgement findByCodeAndContest(String code, Contest contest);

    List<Judgement> findByContest(Contest contest);

    List<Judgement> findByContestOrderByNameAsc(Contest contest);

    /**
     * load all languages and number of submissions for judgment if there is at
     * least one submission per language
     */
    @Query("SELECT NEW org.apache.commons.lang3.tuple.ImmutablePair(tp.language, COUNT(tp)) FROM TeamProblem tp JOIN tp.team t WHERE tp.resultCode = ?1 AND tp.judged = true AND t.contest = ?2 GROUP BY tp.language HAVING COUNT(tp) > 0")
    List<ImmutablePair<String, Long>> getJudgmentReport(String resultCode, Contest contest);

    @Transactional
    @Modifying
    @Query("DELETE FROM Judgement j WHERE j.contest = ?1")
    void deleteByContest(Contest contest);
}
