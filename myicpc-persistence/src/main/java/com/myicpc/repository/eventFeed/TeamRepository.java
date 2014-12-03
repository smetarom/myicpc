package com.myicpc.repository.eventFeed;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByContest(Contest contest);

    @Query("SELECT t FROM Team t WHERE t.contest = ?1 AND t.id IN ?2")
    List<Team> findByContestAndTeamId(Contest contest, List<Long> ids);

    List<Team> findByContestOrderByRankAsc(Contest contest);

    // ---

    Team findByExternalId(Long externalId);

    Team findBySystemIdAndContest(Long systemId, Contest contest);

    @Query(value = "SELECT t FROM Team t ORDER BY t.id ASC")
    List<Team> findAllOrderByIdAsc();

    @Query(value = "SELECT t FROM Team t ORDER BY t.name ASC")
    List<Team> findAllOrderByNameAsc();

    @Query(value = "SELECT t FROM Team t ORDER BY t.rank ASC")
    List<Team> findAllOrderByRankAsc();

    @Query(value = "SELECT t FROM Team t ORDER BY t.problemsSolved DESC, t.totalTime ASC, t.name ASC")
    List<Team> findAllOrderByProblemSolvedAndTotalTime();

    @Query(value = "SELECT t FROM Team t WHERE t IN (SELECT tp.team FROM TeamProblem tp WHERE tp.problem = ?1) ORDER BY t.rank, t.name")
    List<Team> findAllTeamsSolvingProblem(Problem problem);

    @Query(value = "SELECT t FROM Team t WHERE t.externalId NOT IN (SELECT ti.externalId FROM TeamInfo ti)")
    List<Team> getMismatchedTeamsExternalIds();

    @Transactional
    @Modifying
    @Query("DELETE FROM Team t WHERE t.contest = ?1")
    void deleteByContest(Contest contest);
}
