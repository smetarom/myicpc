package com.myicpc.repository.codeInsight;

import com.myicpc.model.codeInsight.CodeInsightActivity;
import com.myicpc.model.contest.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CodeInsightActivityRepository extends JpaRepository<CodeInsightActivity, Long> {

    CodeInsightActivity findByExternalIdAndContest(Long externalId, Contest contest);

    List<CodeInsightActivity> findByModifyTimeBetweenAndContest(int start, int end, Contest contest);

}
