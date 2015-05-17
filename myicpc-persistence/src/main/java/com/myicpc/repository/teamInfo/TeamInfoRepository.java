package com.myicpc.repository.teamInfo;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.teamInfo.RegionalResult;
import com.myicpc.model.teamInfo.TeamInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamInfoRepository extends JpaRepository<TeamInfo, Long> {

    TeamInfo findByExternalIdAndContest(Long externalId, Contest contest);

    List<TeamInfo> findByContest(Contest contest);

    List<TeamInfo> findByContestOrderByNameAsc(Contest contest);

    List<TeamInfo> findByContestOrderByUniversityNameAsc(Contest contest);

    // ---
    TeamInfo findByExternalId(Long externalId);

    TeamInfo findByTeamContestId(Long teamContestId);

    @Query("SELECT rr FROM RegionalResult rr WHERE rr.contestId = ?1 AND rr.teamInfo.externalId = ?2")
    RegionalResult findRegionalResultByContestIdAndTeamInfoExternalId(Long contestId, Long teamInfoExternalId);

    @Query("SELECT ti FROM TeamInfo ti ORDER BY ti.name")
    List<TeamInfo> findAllOrderByName();

    @Query("SELECT ti FROM TeamInfo ti WHERE ti.teamContestId IN ?1 ORDER BY ti.name")
    List<TeamInfo> findAllOrderByName(List<Long> ids);

    @Query("SELECT ti FROM TeamInfo ti ORDER BY ti.university.name")
    List<TeamInfo> findAllOrderByUniversityName();

    @Query("SELECT ti FROM TeamInfo ti WHERE ti.teamContestId IN ?1 ORDER BY ti.university.name")
    List<TeamInfo> findAllOrderByUniversityName(List<Long> ids);

    @Query("SELECT COUNT(ti) FROM TeamInfo ti WHERE ti.teamContestId IS NULL OR ti.teamContestId = 0")
    Long countTeamInfosWithoutTeamContestId();

}
