package com.myicpc.repository.teamInfo;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.model.teamInfo.TeamInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContestParticipantRepository extends CrudRepository<ContestParticipant, Long> {
    ContestParticipant findByExternalId(Long externalId);

    ContestParticipant findByTwitterUsernameIgnoreCase(String twitterUsername);

    ContestParticipant findByVineUsernameIgnoreCase(String vineUsername);

    ContestParticipant findByInstagramUsernameIgnoreCase(String instagramUsername);

    @Query(value = "SELECT DISTINCT tm FROM ContestParticipant tm LEFT JOIN FETCH tm.teamAssociations ta ORDER BY tm.lastname, tm.firstname")
    List<ContestParticipant> findAllOrderByName();

    @Query(value = "SELECT DISTINCT tm FROM ContestParticipant tm LEFT JOIN FETCH tm.teamAssociations ta WHERE ta.teamInfo=?1 AND ta.contestParticipantRole = ?2")
    List<ContestParticipant> findByTeamInfoAndContestParticipantRole(TeamInfo teamInfo, ContestParticipantRole role);

    @Query(value = "SELECT DISTINCT tm FROM ContestParticipant tm LEFT JOIN FETCH tm.teamAssociations ta WHERE ta.contestParticipantRole = ?1 ORDER BY tm.lastname")
    List<ContestParticipant> findByContestParticipantRoleOrderByName(ContestParticipantRole role);

    @Query(value = "SELECT DISTINCT cp " +
            "FROM ContestParticipant cp JOIN cp.teamAssociations ta JOIN ta.teamInfo ti " +
            "WHERE ti.contest = ?1 " +
            "ORDER BY cp.firstname")
    List<ContestParticipant> findByContest(Contest contest);

    @Query(value = "SELECT COUNT(tm) FROM ContestParticipant tm WHERE tm.linkedinOauthToken IS NOT NULL")
    Long countLinkedInSynchronized();

    @Query(value = "SELECT COUNT(tm) FROM ContestParticipant tm WHERE tm.profilePictureUrl IS NOT NULL")
    Long countProfilePictureSynchronized();

    @Query(value = "SELECT COUNT(tm) FROM ContestParticipant tm WHERE tm.twitterUsername IS NOT NULL")
    Long countTwitterSynchronized();

    @Query("SELECT tm FROM ContestParticipant tm WHERE tm.twitterUsername IS NOT NULL ORDER BY RANDOM()")
    List<ContestParticipant> getRandomWithTwitterAccount(Pageable pageable);

    @Query("SELECT tm FROM ContestParticipant tm WHERE tm.vineUsername IS NOT NULL ORDER BY RANDOM()")
    List<ContestParticipant> getRandomWithVineAccount(Pageable pageable);

    @Query("SELECT tm FROM ContestParticipant tm WHERE tm.instagramUsername IS NOT NULL ORDER BY RANDOM()")
    List<ContestParticipant> getRandomWithInstagramAccount(Pageable pageable);
}
