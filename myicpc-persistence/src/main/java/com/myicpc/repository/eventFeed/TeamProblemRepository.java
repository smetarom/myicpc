package com.myicpc.repository.eventFeed;

import com.myicpc.dto.eventFeed.TeamSubmissionDTO;
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
    @Query("SELECT tp FROM TeamProblem tp JOIN FETCH tp.team JOIN FETCH tp.problem WHERE tp.systemId = ?1 AND tp.team.contest = ?2")
    TeamProblem findBySystemIdAndTeamContest(Long systemId, Contest contest);

    List<TeamProblem> findByTeam(Team team);

    List<TeamProblem> findByTeamOrderByTimeDesc(Team team);

    List<TeamProblem> findByProblem(Problem problem);

    @Query("SELECT new com.myicpc.dto.eventFeed.TeamSubmissionDTO(ts.systemId, t.externalId, t.name, ts.solved, ts.penalty, ts.time, ts.judged, ts.numTestPassed, ts.totalNumTests, ts.language, ts.resultCode) " +
            "FROM TeamProblem ts " +
            "JOIN ts.team t " +
            "WHERE ts.problem = ?1 " +
            "ORDER BY ts.systemId DESC")
    List<TeamSubmissionDTO> findTeamSubmissionsByProblem(Problem problem);

    List<TeamProblem> findByProblemOrderByTimeDesc(Problem problem);

    List<TeamProblem> findByProblemAndFirstSolved(Problem problem, boolean firstSolved);

    List<TeamProblem> findByLanguageAndTeamContest(String language, Contest contest);

    @Query(value = "SELECT COUNT(tp) FROM TeamProblem tp WHERE tp.team = ?1 and tp.problem = ?2")
    Long countTeamProblemsByTeamAndProblem(Team team, Problem problem);

    List<TeamProblem> findByTeamAndProblemOrderByTimeAsc(Team team, Problem problem);

    Long countByTeamContest(Contest contest);

    @Query(value = "SELECT MIN(tp.time) FROM TeamProblem tp WHERE tp.team = ?1 AND tp.solved = true")
    Double getLastAcceptedTeamProblemTime(Team team);

    @Transactional
    @Modifying
    @Query("DELETE FROM TeamProblem tp WHERE tp.team IN (SELECT t FROM Team t WHERE t.contest = ?1)")
    void deleteByContest(Contest contest);
}
