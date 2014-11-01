package com.myicpc.repository.teamInfo;

import com.myicpc.enums.TeamMemberRole;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.model.teamInfo.TeamMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamMemberRepository extends CrudRepository<TeamMember, Long> {
    TeamMember findByExternalId(Long externalId);

    TeamMember findByTwitterUsernameIgnoreCase(String twitterUsername);

    TeamMember findByVineUsernameIgnoreCase(String vineUsername);

    TeamMember findByInstagramUsernameIgnoreCase(String instagramUsername);

    @Query(value = "SELECT DISTINCT tm FROM TeamMember tm ORDER BY tm.lastname, tm.firstname")
    List<TeamMember> findAllOrderByName();

    @Query(value = "SELECT DISTINCT tm FROM TeamMember tm LEFT JOIN tm.teamAssociations ta WHERE ta.teamInfo=?1 AND ta.teamMemberRole = ?2")
    List<TeamMember> findByTeamInfoAndTeamMemberRole(TeamInfo teamInfo, TeamMemberRole role);

    @Query(value = "SELECT DISTINCT tm FROM TeamMember tm LEFT JOIN tm.teamAssociations ta WHERE ta.teamMemberRole = ?1")
    List<TeamMember> findByTeamMemberRole(TeamMemberRole role);

    @Query(value = "SELECT DISTINCT tm FROM TeamMember tm LEFT JOIN tm.teamAssociations ta WHERE ta.teamMemberRole = ?1 ORDER BY tm.lastname")
    List<TeamMember> findByTeamMemberRoleOrderByName(TeamMemberRole role);

    @Query(value = "SELECT COUNT(tm) FROM TeamMember tm WHERE tm.linkedinOauthToken IS NOT NULL")
    Long countLinkedInSynchronized();

    @Query(value = "SELECT COUNT(tm) FROM TeamMember tm WHERE tm.profilePictureUrl IS NOT NULL")
    Long countProfilePictureSynchronized();

    @Query(value = "SELECT COUNT(tm) FROM TeamMember tm WHERE tm.twitterUsername IS NOT NULL")
    Long countTwitterSynchronized();

    @Query("SELECT tm FROM TeamMember tm WHERE tm.twitterUsername IS NOT NULL ORDER BY RANDOM()")
    List<TeamMember> getRandomWithTwitterAccount(Pageable pageable);

    @Query("SELECT tm FROM TeamMember tm WHERE tm.vineUsername IS NOT NULL ORDER BY RANDOM()")
    List<TeamMember> getRandomWithVineAccount(Pageable pageable);

    @Query("SELECT tm FROM TeamMember tm WHERE tm.instagramUsername IS NOT NULL ORDER BY RANDOM()")
    List<TeamMember> getRandomWithInstagramAccount(Pageable pageable);
}
