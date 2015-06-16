package com.myicpc.repository.quest;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.teamInfo.ContestParticipant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestParticipantRepository extends PagingAndSortingRepository<QuestParticipant, Long> {
    List<QuestParticipant> findByContest(Contest contest);

    List<QuestParticipant> findByContestOrderByContestParticipantLastnameAsc(Contest contest);

    QuestParticipant findByContestAndContestParticipantTwitterUsernameIgnoreCase(Contest contest, String twitterUsername);

    QuestParticipant findByContestAndContestParticipantVineUsernameIgnoreCase(Contest contest, String vineUsername);

    QuestParticipant findByContestAndContestParticipantInstagramUsernameIgnoreCase(Contest contest, String instagramUsername);

    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN FETCH qp.contestParticipant tm JOIN tm.teamAssociations ta WHERE ta.contestParticipantRole IN ?1 AND qp.contest = ?2 ORDER BY qp.points DESC, qp.tm.firstname ASC")
    List<QuestParticipant> findByRoles(List<ContestParticipantRole> roles, Contest contest, Pageable pageable);

    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.contestParticipant tm JOIN tm.teamAssociations ta WHERE ta.contestParticipantRole = ?1 AND qp.contest = ?2 ORDER BY qp.points DESC, qp.id ASC")
    List<QuestParticipant> findByRoleAndContest(ContestParticipantRole role, Contest contest, Pageable pageable);

    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.contestParticipant tm JOIN tm.teamAssociations ta WHERE ta.contestParticipantRole != ?1 AND qp.contest = ?2 ORDER BY qp.points DESC, qp.id ASC")
    List<QuestParticipant> findByNotRoleAndContest(ContestParticipantRole role, Contest contest, Pageable pageable);

    /**
     * @return Quest participants, which have a role in the system and are not
     * blacklisted
     */
    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.contestParticipant tm JOIN tm.teamAssociations ta WHERE ta.contestParticipantRole = ?1 AND qp.contestParticipant.twitterUsername NOT IN ?2 AND qp.contest = ?3 ORDER BY qp.points DESC")
    List<QuestParticipant> findAvailableByRoleAndContest(ContestParticipantRole role, List<String> usernames, Contest contest, Pageable pageable);

    /**
     * @return Quest participants, which does not have a role in the system and
     * are not blacklisted
     */
    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.contestParticipant tm JOIN tm.teamAssociations ta WHERE ta.contestParticipantRole != ?1 AND qp.contestParticipant.twitterUsername NOT IN ?2 AND qp.contest = ?3 ORDER BY qp.points DESC")
    List<QuestParticipant> findAvailableByNotRoleAndContest(ContestParticipantRole role, List<String> usernames, Contest contest, Pageable pageable);

    @Query("SELECT qp FROM QuestParticipant qp WHERE qp.contest = ?1 ORDER BY RANDOM()")
    List<QuestParticipant> getRandomQuestParticipantByContest(Contest contest, Pageable pageable);

    // ----

    QuestParticipant findByContestParticipant(ContestParticipant contestParticipant);

    @Query("SELECT qp FROM QuestParticipant qp ORDER BY qp.contestParticipant.lastname")
    List<QuestParticipant> findAllOrderByName();

    @Query("SELECT qp FROM QuestParticipant qp ORDER BY qp.points DESC")
    List<QuestParticipant> findAllOrderByPoints();

    @Query("SELECT qp FROM QuestParticipant qp WHERE qp.contestParticipant.twitterUsername NOT IN ?1 ORDER BY points DESC")
    List<QuestParticipant> findAvailableOrderByPoints(List<String> usernames);

    @Query("SELECT qp FROM QuestParticipant qp ORDER BY qp.points DESC")
    List<QuestParticipant> findAllOrderByPoints(Pageable pageable);

    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.contestParticipant tm JOIN tm.teamAssociations ta WHERE ta.contestParticipantRole = ?1 ORDER BY qp.points DESC, qp.id ASC")
    List<QuestParticipant> findByRole(ContestParticipantRole role, Pageable pageable);

    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.contestParticipant tm JOIN tm.teamAssociations ta WHERE ta.contestParticipantRole != ?1 ORDER BY qp.points DESC, qp.id ASC")
    List<QuestParticipant> findByNotRole(ContestParticipantRole role, Pageable pageable);

    /**
     * @return Quest participants, which have a role in the system and are not
     * blacklisted
     */
    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.contestParticipant tm JOIN tm.teamAssociations ta WHERE ta.contestParticipantRole = ?1 AND qp.contestParticipant.twitterUsername NOT IN ?2 ORDER BY qp.points DESC")
    List<QuestParticipant> findAvailableByRole(ContestParticipantRole role, List<String> usernames, Pageable pageable);

    /**
     * @return Quest participants, which does not have a role in the system and
     * are not blacklisted
     */
    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.contestParticipant tm JOIN tm.teamAssociations ta WHERE ta.contestParticipantRole != ?1 AND qp.contestParticipant.twitterUsername NOT IN ?2 ORDER BY qp.points DESC")
    List<QuestParticipant> findAvailableByNotRole(ContestParticipantRole role, List<String> usernames, Pageable pageable);

    @Query("SELECT qp FROM QuestParticipant qp ORDER BY RANDOM()")
    List<QuestParticipant> getRandomQuestParticipant(Pageable pageable);

    QuestParticipant findByContestParticipantTwitterUsernameIgnoreCase(String twitterUsername);

    QuestParticipant findByContestParticipantVineUsernameIgnoreCase(String vineUsername);

    QuestParticipant findByContestParticipantInstagramUsernameIgnoreCase(String instagramUsername);
}
