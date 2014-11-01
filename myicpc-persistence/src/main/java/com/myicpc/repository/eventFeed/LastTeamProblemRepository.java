package com.myicpc.repository.eventFeed;

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

    @Transactional
    @Modifying
    @Query("DELETE FROM LastTeamProblem ltp WHERE ltp.team IN (SELECT t FROM Team t WHERE t.contest = ?1)")
    void deleteByContest(Contest contest);
}
