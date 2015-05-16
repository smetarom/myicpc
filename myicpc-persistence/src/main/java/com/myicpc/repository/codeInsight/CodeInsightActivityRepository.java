package com.myicpc.repository.codeInsight;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.codeInsight.CodeInsightActivity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CodeInsightActivityRepository extends CrudRepository<CodeInsightActivity, Long> {
    CodeInsightActivity findByExternalId(Long externalId);

    List<CodeInsightActivity> findByModifyTimeBetween(int start, int end);

    @Transactional
    @Modifying
    @Query("DELETE FROM CodeInsightActivity ea WHERE ea.team IN (SELECT t FROM Team t WHERE t.contest = ?1)")
    void deleteByContest(Contest contest);
}
