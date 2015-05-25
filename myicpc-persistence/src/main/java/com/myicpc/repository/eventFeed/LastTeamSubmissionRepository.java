package com.myicpc.repository.eventFeed;

import com.myicpc.model.eventFeed.LastTeamSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LastTeamSubmissionRepository extends JpaRepository<LastTeamSubmission, Long> {
    LastTeamSubmission findByTeamIdAndProblemId(long teamId, long problemId);

    List<LastTeamSubmission> findByContestId(long contestId);

    @Transactional
    @Modifying
    @Query("DELETE FROM LastTeamSubmission lts WHERE lts.contestId = ?1")
    void deleteByContestId(Long contestId);
}
