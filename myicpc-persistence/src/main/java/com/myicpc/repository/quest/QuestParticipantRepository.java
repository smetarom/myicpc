package com.myicpc.repository.quest;

import com.myicpc.enums.TeamMemberRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.teamInfo.TeamMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestParticipantRepository extends PagingAndSortingRepository<QuestParticipant, Long> {
    List<QuestParticipant> findByContest(Contest contest);

    List<QuestParticipant> findByContestOrderByTeamMemberLastnameAsc(Contest contest);

    QuestParticipant findByContestAndTeamMemberTwitterUsernameIgnoreCase(Contest contest, String twitterUsername);

    QuestParticipant findByContestAndTeamMemberVineUsernameIgnoreCase(Contest contest, String vineUsername);

    QuestParticipant findByContestAndTeamMemberInstagramUsernameIgnoreCase(Contest contest, String instagramUsername);

    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.teamMember tm JOIN tm.teamAssociations ta WHERE ta.teamMemberRole = ?1 AND qp.contest = ?2 ORDER BY qp.points DESC, qp.id ASC")
    List<QuestParticipant> findByRoleAndContest(TeamMemberRole role, Contest contest, Pageable pageable);

    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.teamMember tm JOIN tm.teamAssociations ta WHERE ta.teamMemberRole != ?1 AND qp.contest = ?2 ORDER BY qp.points DESC, qp.id ASC")
    List<QuestParticipant> findByNotRoleAndContest(TeamMemberRole role, Contest contest, Pageable pageable);

    /**
     * @return Quest participants, which have a role in the system and are not
     * blacklisted
     */
    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.teamMember tm JOIN tm.teamAssociations ta WHERE ta.teamMemberRole = ?1 AND qp.teamMember.twitterUsername NOT IN ?2 AND qp.contest = ?3 ORDER BY qp.points DESC")
    List<QuestParticipant> findAvailableByRoleAndContest(TeamMemberRole role, List<String> usernames, Contest contest, Pageable pageable);

    /**
     * @return Quest participants, which does not have a role in the system and
     * are not blacklisted
     */
    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.teamMember tm JOIN tm.teamAssociations ta WHERE ta.teamMemberRole != ?1 AND qp.teamMember.twitterUsername NOT IN ?2 AND qp.contest = ?3 ORDER BY qp.points DESC")
    List<QuestParticipant> findAvailableByNotRoleAndContest(TeamMemberRole role, List<String> usernames, Contest contest, Pageable pageable);

    @Query("SELECT qp FROM QuestParticipant qp WHERE qp.contest = ?1 ORDER BY RANDOM()")
    List<QuestParticipant> getRandomQuestParticipantByContest(Contest contest, Pageable pageable);

    // ----

    QuestParticipant findByTeamMember(TeamMember teamMember);

    @Query("SELECT qp FROM QuestParticipant ORDER BY qp.teamMember.lastname")
    List<QuestParticipant> findAllOrderByName();

    @Query("SELECT qp FROM QuestParticipant ORDER BY qp.points DESC")
    List<QuestParticipant> findAllOrderByPoints();

    @Query("SELECT qp FROM QuestParticipant qp WHERE qp.teamMember.twitterUsername NOT IN ?1 ORDER BY points DESC")
    List<QuestParticipant> findAvailableOrderByPoints(List<String> usernames);

    @Query("SELECT qp FROM QuestParticipant qp ORDER BY qp.points DESC")
    List<QuestParticipant> findAllOrderByPoints(Pageable pageable);

    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.teamMember tm JOIN tm.teamAssociations ta WHERE ta.teamMemberRole = ?1 ORDER BY qp.points DESC, qp.id ASC")
    List<QuestParticipant> findByRole(TeamMemberRole role, Pageable pageable);

    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.teamMember tm JOIN tm.teamAssociations ta WHERE ta.teamMemberRole != ?1 ORDER BY qp.points DESC, qp.id ASC")
    List<QuestParticipant> findByNotRole(TeamMemberRole role, Pageable pageable);

    /**
     * @return Quest participants, which have a role in the system and are not
     * blacklisted
     */
    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.teamMember tm JOIN tm.teamAssociations ta WHERE ta.teamMemberRole = ?1 AND qp.teamMember.twitterUsername NOT IN ?2 ORDER BY qp.points DESC")
    List<QuestParticipant> findAvailableByRole(TeamMemberRole role, List<String> usernames, Pageable pageable);

    /**
     * @return Quest participants, which does not have a role in the system and
     * are not blacklisted
     */
    @Query("SELECT DISTINCT qp FROM QuestParticipant qp JOIN qp.teamMember tm JOIN tm.teamAssociations ta WHERE ta.teamMemberRole != ?1 AND qp.teamMember.twitterUsername NOT IN ?2 ORDER BY qp.points DESC")
    List<QuestParticipant> findAvailableByNotRole(TeamMemberRole role, List<String> usernames, Pageable pageable);

    @Query("SELECT qp FROM QuestParticipant qp ORDER BY RANDOM()")
    List<QuestParticipant> getRandomQuestParticipant(Pageable pageable);

    QuestParticipant findByTeamMemberTwitterUsernameIgnoreCase(String twitterUsername);

    QuestParticipant findByTeamMemberVineUsernameIgnoreCase(String vineUsername);

    QuestParticipant findByTeamMemberInstagramUsernameIgnoreCase(String instagramUsername);
}
