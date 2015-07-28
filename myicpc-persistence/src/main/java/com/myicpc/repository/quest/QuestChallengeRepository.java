package com.myicpc.repository.quest;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * DAO repository for {@link QuestChallenge}
 *
 * @author Roman Smetana
 */
public interface QuestChallengeRepository extends JpaRepository<QuestChallenge, Long> {
    List<QuestChallenge> findByContest(Contest contest);

    List<QuestChallenge> findByContest(Contest contest, Sort sort);

    List<QuestChallenge> findByContestOrderByNameAsc(Contest contest);

    QuestChallenge findByHashtagSuffix(String hashtag, Contest contest);

    /**
     * Finds quest challenges, which have started and are not yet over
     *
     * @param now     current date
     * @param contest contest
     * @return available quest challenges at {@code now}
     */
    @Query("SELECT qc " +
            "FROM QuestChallenge qc " +
            "WHERE qc.startDate <= ?1 " +
            "   AND (qc.endDate IS NULL OR qc.endDate >= ?1) " +
            "   AND qc.contest = ?2 " +
            "ORDER BY qc.endDate")
    List<QuestChallenge> findByContestAvailableChallenges(Date now, Contest contest);

    /**
     * Finds quest challenges, which have started
     *
     * @param now     current date
     * @param contest contest
     * @return open quest challenges, sorted by name
     */
    @Query("SELECT qc " +
            "FROM QuestChallenge qc " +
            "WHERE qc.startDate <= ?1 " +
            "   AND qc.contest = ?2 " +
            "ORDER BY qc.name")
    List<QuestChallenge> findOpenChallengesByContestOrderByName(Date now, Contest contest);

    /**
     * Finds quest challenges, which have started
     *
     * @param now     current date
     * @param contest contest
     * @return open quest challenges, sorted by hashtag
     */
    @Query("SELECT qc " +
            "FROM QuestChallenge qc " +
            "WHERE qc.startDate <= ?1 " +
            "   AND qc.contest = ?2 " +
            "ORDER BY qc.hashtagSuffix")
    List<QuestChallenge> findOpenChallengesByContestOrderByHashtag(Date now, Contest contest);

    /**
     * Finds quest challenges for {@code questParticipant}, which this questParticipant did not
     * solve yet
     *
     * @param now              current date
     * @param questParticipant quest participant
     * @param contest          contest
     * @return not solved quest challenges for quest participant
     */
    @Query("SELECT qc FROM QuestChallenge qc " +
            "WHERE qc.startDate <= ?1 AND qc.contest = ?3 " +
            "   AND qc NOT IN (SELECT qs.challenge FROM QuestSubmission qs WHERE qs.submissionState = 'ACCEPTED' AND qs.participant = ?2)" +
            "ORDER BY qc.name")
    List<QuestChallenge> findAvailableChallengesByQuestParticipantOrderByName(Date now, QuestParticipant questParticipant, Contest contest);

    /**
     * Finds non published challenges, opened at {@code date}
     *
     * @param date    date, when non published challenges are searched
     * @param contest contest
     * @return non published challenges
     */
    @Query("SELECT qc " +
            "FROM QuestChallenge qc " +
            "WHERE qc.published = false " +
            "   AND qc.contest = ?2 " +
            "   AND qc.startDate < ?1")
    List<QuestChallenge> findAllNonpublishedStartedChallenges(Date date, Contest contest);
}
