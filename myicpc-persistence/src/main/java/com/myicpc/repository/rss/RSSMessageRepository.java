package com.myicpc.repository.rss;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.rss.RSSFeed;
import com.myicpc.model.rss.RSSMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RSSMessageRepository extends PagingAndSortingRepository<RSSMessage, Long> {

    Page<RSSMessage> findByRssFeedContestOrderByPublishDateDesc(Contest contest, Pageable pageable);

    Page<RSSMessage> findByRssFeedAndRssFeedContestOrderByPublishDateDesc(RSSFeed rssFeed, Contest contest, Pageable pageable);

    // ----------

    Iterable<RSSMessage> findByRssFeedOrderByPublishDateDesc(RSSFeed rssFeed);

    Page<RSSMessage> findByRssFeedOrderByPublishDateDesc(RSSFeed rssFeed, Pageable pageable);

    @Query(value = "SELECT rm FROM RSSMessage rm ORDER BY rm.publishDate DESC")
    Page<RSSMessage> findAllOrderByPublishDateDesc(Pageable pageable);

    @Query(value = "SELECT COUNT(rm) FROM RSSMessage rm WHERE rm.rssFeed = ?1")
    Long countByRSSFeed(RSSFeed rssFeed);

    @Query("SELECT rm FROM RSSMessage rm ORDER BY rm.id DESC")
    Page<RSSMessage> findLatestRSSMessages(Pageable pageable);
}
