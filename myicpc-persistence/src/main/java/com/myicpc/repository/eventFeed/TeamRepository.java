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
    List<Team> findByContestAndTeamIds(Contest contest, List<Long> ids);

    Team findBySystemIdAndContest(Long systemId, Contest contest);

    Long countByContest(Contest contest);

    @Transactional
    @Modifying
    @Query("DELETE FROM Team t WHERE t.contest = ?1")
    void deleteByContest(Contest contest);
}
