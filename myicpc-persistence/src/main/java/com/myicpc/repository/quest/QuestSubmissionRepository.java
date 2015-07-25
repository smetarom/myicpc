package com.myicpc.repository.quest;

import com.myicpc.dto.quest.QuestSubmissionDTO;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestSubmissionRepository extends JpaRepository<QuestSubmission, Long>, QuestSubmissionDAO {
    @Query("SELECT qs FROM QuestSubmission qs WHERE qs.submissionState = 'ACCEPTED' AND qs.voteSubmissionState = 'VOTE_WINNER' AND qs.challenge.contest = ?1")
    List<QuestSubmission> getVoteWinnersSubmissions(Contest contest);

    @Query("SELECT qs FROM QuestSubmission qs WHERE qs.submissionState = 'ACCEPTED' AND qs.voteSubmissionState = 'IN_PROGRESS' AND qs.challenge.contest = ?1")
    List<QuestSubmission> getVotesInProgressSubmissions(Contest contest);

    @Query("SELECT MAX(qs.notification.id) FROM QuestSubmission qs WHERE qs.challenge.contest = ?1")
    Long getMaxNotificationId(Contest contest);

    List<QuestSubmission> findByChallengeAndSubmissionStateOrderByNotificationTimestampDesc(QuestChallenge challenge, QuestSubmission.QuestSubmissionState submissionState);

    @Query("SELECT COUNT(qs) FROM QuestSubmission qs WHERE qs.voteSubmissionState = 'VOTE_WINNER' AND qs.challenge.contest = ?1")
    Long countWinningSubmissions(Contest contest);

    @Query("SELECT qs FROM QuestSubmission qs WHERE qs.submissionState = 'ACCEPTED' AND qs.voteSubmissionState = 'IN_PROGRESS' AND qs.challenge.contest = ?1")
    List<QuestSubmission> getVoteInProgressSubmissions(Contest contest);

    @Query("SELECT qs FROM QuestSubmission qs WHERE qs.submissionState = 'ACCEPTED' AND qs.voteSubmissionState IS NULL AND qs.challenge.contest = ?1 ORDER BY RANDOM()")
    List<QuestSubmission> getVoteEligableSubmissions(Contest contest, Pageable pageable);

    @Query("SELECT new com.myicpc.dto.quest.QuestSubmissionDTO(" +
            "   p.id," +
            "   qs.challenge.id," +
            "   qs.submissionState," +
            "   qs.reasonToReject" +
            ") " +
            "FROM QuestSubmission qs " +
            "   JOIN qs.participant p " +
            "WHERE p.contest = ?2 " +
            "   AND p.id IN ?1")
    List<QuestSubmissionDTO> findQuestSubmissionDTOByQuestParticipantId(List<Long> participantIds, Contest contest);

    QuestSubmission findByChallengeAndParticipant(QuestChallenge challenge, QuestParticipant participant);
}
