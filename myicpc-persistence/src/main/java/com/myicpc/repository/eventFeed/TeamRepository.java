package com.myicpc.repository.eventFeed;

import com.myicpc.dto.eventFeed.TeamDTO;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("SELECT t FROM Team t WHERE t.contest = ?1")
    List<Team> findByContest(Contest contest);

    List<Team> findByContestOrderByNameAsc(Contest contest);

    @Query("SELECT new com.myicpc.dto.eventFeed.TeamDTO(t.id, t.externalId, t.rank, t.name, t.nationality, t.problemsSolved, t.totalTime, u.externalId, u.name, r.externalId, r.name) " +
            "FROM Team t LEFT JOIN t.teamInfo ti LEFT JOIN ti.university u LEFT JOIN ti.region r " +
            "WHERE t.contest = ?1")
    List<TeamDTO> findTeamDTOByContest(Contest contest);

    @Query("SELECT t FROM Team t WHERE t.contest = ?1 AND t.id IN ?2")
    List<Team> findByContestAndTeamIds(Contest contest, List<Long> ids);

    @Query("SELECT t FROM Team t WHERE t.systemId = ?1 AND t.contest = ?2")
    Team findBySystemIdAndContest(Long systemId, Contest contest);

    Team findByExternalId(Long externalId);

    Long countByContest(Contest contest);

    @Transactional
    @Modifying
    @Query("DELETE FROM Team t WHERE t.contest = ?1")
    void deleteByContest(Contest contest);
}
