package com.myicpc.repository.eventFeed;

import com.myicpc.model.eventFeed.Judgement;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JudgementRepository extends CrudRepository<Judgement, Long> {
    Judgement findByCode(String code);

    @Query("SELECT j FROM Judgement j ORDER BY j.id DESC")
    List<Judgement> findAllOrderByIdDesc();

    /**
     * load all languages and number of submissions for judgment if there is at
     * least one submission per language
     */
    @Query("SELECT NEW org.apache.commons.lang3.tuple.ImmutablePair(tp.language, COUNT(tp)) FROM TeamProblem tp WHERE tp.resultCode = ?1 AND tp.judged = true GROUP BY tp.language HAVING COUNT(tp) > 0")
    List<ImmutablePair<String, Long>> getJudgmentReport(String resultCode);
}
