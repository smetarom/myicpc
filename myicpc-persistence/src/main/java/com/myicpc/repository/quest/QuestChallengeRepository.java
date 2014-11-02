package com.myicpc.repository.quest;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface QuestChallengeRepository extends CrudRepository<QuestChallenge, Long> {
    List<QuestChallenge> findByContest(Contest contest, Sort sort);

    List<QuestChallenge> findByContestOrderByNameAsc(Contest contest);

    @Query("SELECT qc FROM QuestChallenge qc WHERE qc.startDate < ?1 AND (qc.endDate IS NULL OR qc.endDate > ?1) AND qc.contest = ?2 ORDER BY qc.endDate")
    List<QuestChallenge> findByContestAvailableChallenges(Date now, Contest contest);

    @Query("SELECT qc FROM QuestChallenge qc WHERE qc.startDate < ?1 AND qc.contest = ?2 ORDER BY qc.name")
    List<QuestChallenge> findOpenChallengesByContestOrderByName(Date now, Contest contest);

    @Query("SELECT qc FROM QuestChallenge qc WHERE qc.startDate < ?1 AND qc.contest = ?2 ORDER BY qc.hashtagSuffix")
    List<QuestChallenge> findOpenChallengesByContestOrderByHashtag(Date now, Contest contest);

    // ----

    @Query("SELECT qc FROM QuestChallenge qc ORDER BY qc.name")
    List<QuestChallenge> findAllOrderByName();

    @Query("SELECT qc FROM QuestChallenge qc WHERE qc.startDate < ?1 ORDER BY qc.name")
    List<QuestChallenge> findOpenChallengesOrderByName(Date now);

    @Query("SELECT qc FROM QuestChallenge qc ORDER BY qc.startDate, qc.name")
    List<QuestChallenge> findAllOrderByStartDate();

    @Query("SELECT qc FROM QuestChallenge qc WHERE qc.startDate < ?1 ORDER BY qc.endDate")
    List<QuestChallenge> findOpenChallenges(Date now);

    @Query("SELECT qc FROM QuestChallenge qc WHERE qc.startDate < ?1 ORDER BY qc.hashtagSuffix")
    List<QuestChallenge> findOpenChallengesOrderByHashtag(Date now);

    @Query("SELECT qc FROM QuestChallenge qc WHERE qc.startDate < ?1 AND (qc.endDate IS NULL OR qc.endDate > ?1) ORDER BY qc.endDate")
    List<QuestChallenge> findAllAvailableChallenges(Date now);

    @Query("SELECT qc FROM QuestChallenge qc ORDER BY RANDOM()")
    List<QuestChallenge> getRandomQuestChallenge(Pageable pageable);

    @Query("SELECT qc FROM QuestChallenge qc ORDER BY challengeType DESC, endDate ASC")
    List<QuestChallenge> getLeaderboardChallenges();

    QuestChallenge findByHashtagSuffix(String hashtag);

    @Query(value = "SELECT qc FROM QuestChallenge qc WHERE qc.published = false AND qc.startDate < ?1")
    List<QuestChallenge> findAllNonpublishedStartedChallenges(Date date);
}
