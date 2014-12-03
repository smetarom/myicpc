package com.myicpc.repository.eventFeed;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Roman Smetana
 */
public interface EventFeedControlRepository extends JpaRepository<EventFeedControl, Long> {
    EventFeedControl findByContest(Contest contest);

    @Transactional
    @Modifying
    @Query("DELETE FROM EventFeedControl efc WHERE efc.contest = ?1")
    void deleteByContest(Contest contest);
}
