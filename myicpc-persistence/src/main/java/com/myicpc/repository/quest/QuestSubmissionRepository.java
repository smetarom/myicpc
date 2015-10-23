package com.myicpc.repository.quest;

import com.myicpc.dto.quest.QuestSubmissionDTO;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.social.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * DAO repository for {@link QuestSubmission}
 *
 * @author Roman Smetana
 */
public interface QuestSubmissionRepository extends JpaRepository<QuestSubmission, Long>, QuestSubmissionDAO {
    List<QuestSubmission> findByChallenge(QuestChallenge questChallenge);

    QuestSubmission findByChallengeAndParticipant(QuestChallenge challenge, QuestParticipant participant);

    List<QuestSubmission> findByChallengeAndSubmissionStateOrderByNotificationTimestampDesc(QuestChallenge challenge, QuestSubmission.QuestSubmissionState submissionState);

    /**
     * Finds a {@link QuestSubmission}s, which have won a voting round
     * <p/>
     * A submission is a vote winner, if it is accepted and marked as 'VOTE_WINNER'
     *
     * @param contest contest
     * @return submissions which have won
     */
    @Query("SELECT qs " +
            "FROM QuestSubmission qs " +
            "WHERE qs.submissionState = 'ACCEPTED' " +
            "   AND qs.voteSubmissionState = 'VOTE_WINNER' " +
            "   AND qs.challenge.contest = ?1")
    List<QuestSubmission> getVoteWinnersSubmissions(Contest contest);

    /**
     * Finds a {@link QuestSubmission}s, which are in the current voting round
     * <p/>
     * A submission is in the current voting round, if it is accepted and marked as 'IN_PROGRESS'
     *
     * @param contest contest
     * @return submissions which are in the currnt voting round
     */
    @Query("SELECT qs " +
            "FROM QuestSubmission qs " +
            "WHERE qs.submissionState = 'ACCEPTED' " +
            "   AND qs.voteSubmissionState = 'IN_PROGRESS' " +
            "   AND qs.challenge.contest = ?1")
    List<QuestSubmission> getVoteInProgressSubmissions(Contest contest);

    /**
     * Finds eligible {@link QuestSubmission}s for voting round
     * <p/>
     * The submission is eligible, if it is accepted and it is not in state 'IN_PROGRESS' or 'VOTE_WINNER'
     *
     * @param contest  contest
     * @param pageable pageable
     * @return eligible submissions for voting round
     */
    @Query("SELECT qs " +
            "FROM QuestSubmission qs " +
            "WHERE qs.submissionState = 'ACCEPTED' " +
            "   AND qs.voteSubmissionState IS NULL " +
            "   AND qs.challenge.contest = ?1 ORDER BY RANDOM()")
    List<QuestSubmission> getVoteEligibleSubmissions(Contest contest, Pageable pageable);

    /**
     * Finds a maximum {@link Notification#id}, which is contained in any quest submissions in {@code contest}
     *
     * @param contest contest
     * @return maximum notification ID, or {@code null} if no notification exists
     */
    @Query("SELECT MAX(qs.notification.id) FROM QuestSubmission qs WHERE qs.challenge.contest = ?1")
    Long getMaxNotificationId(Contest contest);

    /**
     * Finds {@link QuestSubmissionDTO}s for {@code participantIds}
     * <p/>
     * It uses {@link QuestSubmissionDTO} to optimize the performance and selects
     * only data necessary for leaderboard
     *
     * @param participantIds list of participant IDs for which find submissions
     * @param contest        contest
     * @return participants quest submissions
     */
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
}
