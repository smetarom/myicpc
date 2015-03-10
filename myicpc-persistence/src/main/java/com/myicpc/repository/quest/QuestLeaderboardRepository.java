package com.myicpc.repository.quest;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestLeaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestLeaderboardRepository extends JpaRepository<QuestLeaderboard, Long> {
    List<QuestLeaderboard> findByContestOrderByNameAsc(Contest contest);
}
