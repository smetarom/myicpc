package com.myicpc.repository.editActivity;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.codeInsight.CodeInsightActivity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CodeInsightActivityRepository extends CrudRepository<CodeInsightActivity, Long> {
    CodeInsightActivity findByExternalId(Long externalId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CodeInsightActivity ea WHERE ea.team IN (SELECT t FROM Team t WHERE t.contest = ?1)")
    void deleteByContest(Contest contest);
}
