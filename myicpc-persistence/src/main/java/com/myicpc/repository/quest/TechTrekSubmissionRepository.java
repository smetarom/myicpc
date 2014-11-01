package com.myicpc.repository.quest;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.TechTrekSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechTrekSubmissionRepository extends JpaRepository<TechTrekSubmission, Long> {
    List<TechTrekSubmission> findByContest(Contest contest);
}
