package com.myicpc.repository.eventFeed;

import com.myicpc.dto.eventFeed.LastTeamSubmissionDTO;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.LastTeamProblem;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LastTeamProblemRepository extends JpaRepository<LastTeamProblem, Long> {
    LastTeamProblem findByTeamAndProblem(Team team, Problem problem);

    List<LastTeamProblem> findByTeam(Team team);

    @Query("SELECT " +
            "new com.myicpc.dto.eventFeed.LastTeamSubmissionDTO(" +
            "   ltp.contest.id, " +
            "   ltp.team.id, " +
            "   ltp.problem.id," +
            "   ltp.teamProblem.id, " +
            "   ltp.teamProblem.solved, " +
            "   ltp.teamProblem.penalty, " +
            "   ltp.teamProblem.attempts, " +
            "   ltp.teamProblem.time, " +
            "   ltp.teamProblem.judged, " +
            "   ltp.teamProblem.firstSolved) " +
            "FROM LastTeamProblem ltp WHERE ltp.contest = ?1")
    List<LastTeamSubmissionDTO> findLastTeamSubmissionDTOByContest(Contest contest);

    @Transactional
    @Modifying
    @Query("DELETE FROM LastTeamProblem ltp WHERE ltp.team IN (SELECT t FROM Team t WHERE t.contest = ?1)")
    void deleteByContest(Contest contest);
}
