package com.myicpc.repository.eventFeed;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface TeamProblemRepository extends JpaRepository<TeamProblem, Long>, Serializable {
    TeamProblem findBySystemIdAndTeamContest(Long systemId, Contest contest);

    List<TeamProblem> findByTeam(Team team);

    List<TeamProblem> findByTeamOrderByTimeDesc(Team team);

    List<TeamProblem> findByTeamAndProblemOrderByTimeDesc(Team team, Problem problem);

    List<TeamProblem> findByJudgedOrderByTimeAsc(Boolean judged);

    List<TeamProblem> findByProblem(Problem problem);

    List<TeamProblem> findByProblemAndFirstSolved(Problem problem, boolean firstSolved);

    List<TeamProblem> findByLanguage(String language);

    @Query(value = "SELECT COUNT(tp) FROM TeamProblem tp WHERE tp.team = ?1 and tp.problem = ?2")
    Long countTeamProblemsByTeamAndProblem(Team team, Problem problem);

    List<TeamProblem> findByTeamAndProblemOrderByTimeAsc(Team team, Problem problem);

    @Query(value = "SELECT MIN(tp.time) FROM TeamProblem tp WHERE tp.team = ?1 AND tp.solved = true")
    Double getLastAcceptedTeamProblemTime(Team team);

    @Query(value = "SELECT tp FROM TeamProblem tp ORDER BY tp.id DESC")
    Page<TeamProblem> getLatestTeamProblems(Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM TeamProblem tp WHERE tp.team IN (SELECT t FROM Team t WHERE t.contest = ?1)")
    void deleteByContest(Contest contest);
}
