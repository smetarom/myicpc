package com.myicpc.repository.social;

import com.myicpc.model.social.TwitterMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TwitterMessageRepository extends PagingAndSortingRepository<TwitterMessage, Long> {
    TwitterMessage findByTweetId(Long tweetId);

    List<TwitterMessage> findByTweetIdLessThanEqual(Long tweetId);

    List<TwitterMessage> findByUsername(String username);

    List<TwitterMessage> findByRetweetedId(Long retweetedId);

    @Query("SELECT tm FROM TwitterMessage tm WHERE tm.retweetedId IN ?1")
    List<TwitterMessage> findByRetweetedIds(List<Long> retweetedIds);

    @Query(value = "SELECT tm FROM TwitterMessage tm WHERE UPPER(tm.hashtags) LIKE UPPER(?1) ORDER BY tm.tweetId DESC")
    List<TwitterMessage> findByHashTag(String hashtag1);

    @Query(value = "SELECT tm FROM TwitterMessage tm WHERE UPPER(tm.hashtags) LIKE UPPER(?1) AND UPPER(tm.hashtags) LIKE UPPER(?2) ORDER BY tm.tweetId DESC")
    List<TwitterMessage> findByHashTag(String hashtag1, String hashtag2);

    @Query(value = "SELECT tm FROM TwitterMessage tm WHERE UPPER(tm.hashtags) LIKE UPPER(?1) AND UPPER(tm.hashtags) LIKE UPPER(?2) AND UPPER(tm.hashtags) LIKE UPPER(?3) ORDER BY tm.tweetId DESC")
    List<TwitterMessage> findByHashTag(String hashtag1, String hashtag2, String hashtag3);

    @Query(value = "SELECT tm FROM TwitterMessage tm WHERE UPPER(tm.hashtags) LIKE UPPER(?1) AND UPPER(tm.hashtags) LIKE UPPER(?2) ORDER BY tm.tweetId DESC")
    List<TwitterMessage> findByHashTag(String hashtag1, String hashtag2, Pageable pageable);

    @Query(value = "SELECT tm FROM TwitterMessage tm WHERE UPPER(tm.hashtags) LIKE UPPER(?1) AND UPPER(tm.hashtags) LIKE UPPER(?2) AND tm.id > ?3 ORDER BY tm.tweetId DESC")
    List<TwitterMessage> findByHashTagSinceId(String hashtag1, String hashtag2, long sinceId);

    @Query("SELECT tm FROM TwitterMessage tm ORDER BY tm.id DESC")
    Page<TwitterMessage> findLatestTwitterMessages(Pageable pageable);

}
