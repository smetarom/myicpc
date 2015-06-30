package com.myicpc.repository.eventFeed;

import com.myicpc.dto.eventFeed.TeamSubmissionDTO;
import com.myicpc.dto.insight.InsightSubmissionDTO;
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
    @Query("SELECT tp FROM TeamProblem tp JOIN FETCH tp.team t JOIN FETCH tp.problem p WHERE tp.systemId = ?1 AND t.contest = ?2")
    TeamProblem findBySystemIdAndTeamContest(Long systemId, Contest contest);

    @Query("SELECT tp.judged FROM TeamProblem tp JOIN tp.team t WHERE tp.systemId = ?1 AND t.contest = ?2")
    Boolean getJudgedBySystemIdAndTeamContest(Long systemId, Contest contest);

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

    @Query(value = "SELECT new com.myicpc.dto.insight.InsightSubmissionDTO(t.externalId, t.name, ts.time, ts.solved, ts.firstSolved, ts.language) " +
            "FROM TeamProblem ts " +
            "JOIN ts.team t " +
            "WHERE ts.problem = ?1")
    List<InsightSubmissionDTO> findByProblemInsight(Problem problem);

    @Query(value = "SELECT new com.myicpc.dto.insight.InsightSubmissionDTO(t.externalId, t.name, ts.time, ts.solved, ts.firstSolved, ts.language) " +
                    "FROM TeamProblem ts " +
                    "JOIN ts.team t " +
                    "WHERE ts.language = ?1 " +
                    "   AND t.contest = ?2")
    List<InsightSubmissionDTO> findByLanguageAndContest(String language, Contest contest);

    @Query(value = "SELECT COUNT(tp) FROM TeamProblem tp WHERE tp.team = ?1 and tp.problem = ?2")
    Long countTeamProblemsByTeamAndProblem(Team team, Problem problem);

    @Query("SELECT ts FROM TeamProblem ts WHERE ts.team = ?1 AND ts.problem = ?2 ORDER BY ts.time ASC")
    List<TeamProblem> findByTeamAndProblemOrderByTimeAsc(Team team, Problem problem);

    Long countByTeamContest(Contest contest);

    @Query(value = "SELECT MIN(tp.time) FROM TeamProblem tp WHERE tp.team = ?1 AND tp.solved = true")
    Double getLastAcceptedTeamProblemTime(Team team);

    @Transactional
    @Modifying
    @Query("DELETE FROM TeamProblem tp WHERE tp.team IN (SELECT t FROM Team t WHERE t.contest = ?1)")
    void deleteByContest(Contest contest);

    List<TeamProblem> findByLanguage(String language);
}
