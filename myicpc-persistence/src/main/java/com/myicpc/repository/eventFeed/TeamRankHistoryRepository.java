package com.myicpc.repository.eventFeed;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamRankHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeamRankHistoryRepository extends JpaRepository<TeamRankHistory, Long> {
    List<TeamRankHistory> findByTeam(Team team);

    List<TeamRankHistory> findByTeamContest(Contest contest);

    @Transactional
    @Modifying
    @Query("DELETE FROM TeamRankHistory trh WHERE trh.team IN (SELECT t FROM Team t WHERE t.contest = ?1)")
    void deleteByContest(Contest contest);
}
