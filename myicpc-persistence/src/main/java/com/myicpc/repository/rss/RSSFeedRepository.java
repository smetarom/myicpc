package com.myicpc.repository.rss;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.rss.RSSFeed;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RSSFeedRepository extends CrudRepository<RSSFeed, Long> {
    Iterable<RSSFeed> findByContest(Contest contest);

    Iterable<RSSFeed> findByContestOrderByNameAsc(Contest contest);

    @Query(value = "SELECT rf FROM RSSFeed rf ORDER BY rf.name ASC")
    Iterable<RSSFeed> findAllOrderByNameAsc();

}
