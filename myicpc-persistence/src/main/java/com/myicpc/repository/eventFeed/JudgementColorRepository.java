package com.myicpc.repository.eventFeed;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Judgement;
import com.myicpc.model.eventFeed.JudgementColor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JudgementColorRepository extends JpaRepository<JudgementColor, Long> {
    JudgementColor findByCodeAndContest(String code, Contest contest);

    List<JudgementColor> findByContest(Contest contest);
}
