package com.myicpc.repository.social;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {
    /**
     * Finds notifications of the given type {@code notificationType}, which where generated from entity with ID {@code entityId}
     */
    List<Notification> findByContestAndEntityIdAndNotificationType(Contest contest, Long entityId, NotificationType notificationType);

    /**
     * Finds notifications of the given type {@code notificationType}, which where generated from external source with ID {@code entityId}
     */
    Notification findByContestAndExternalIdAndNotificationType(Contest contest, String externalId, NotificationType notificationType);

    long countByContestAndExternalIdAndNotificationType(Contest contest, String externalId, NotificationType notificationType);

    /**
     * Finds notifications, which were generated from {@code entityIds} and have {@code notificationTypes}
     */
    @Query("SELECT n FROM Notification n WHERE n.entityId IN ?1 AND n.notificationType IN ?2")
    List<Notification> findByEntityIdsAndTypes(List<Long> entityIds, List<NotificationType> notificationTypes, Sort sort);

    /**
     * Finds notifications with {@code notificationTypes}
     */
    @Query("SELECT n FROM Notification n WHERE n.contest = ?2 AND n.deleted = false AND n.offensive = false AND n.notificationType IN ?1 ORDER BY n.id DESC")
    Page<Notification> findByNotificationTypesOrderByIdDesc(List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.timestamp < ?1 AND n.contest = ?3 AND n.deleted = false AND n.offensive = false AND n.notificationType IN ?2 ORDER BY n.timestamp DESC")
    Page<Notification> findByNotificationTypesOrderByIdDesc(Date timestamp, List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.notificationType IN ?2 AND n.contest = ?3 AND n.id < ?1 ORDER BY n.id DESC")
    Page<Notification> findByNotificationTypesFromNotificationIdOrderByIdDesc(Long lastNotificationId, List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.notificationType IN ?1 AND n.contest = ?2 AND (n.imageUrl IS NOT NULL OR n.videoUrl IS NOT NULL) AND n.deleted = false ORDER BY n.id DESC")
    Page<Notification> findByGalleryNotificationTypesOrderByIdDesc(List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.notificationType IN ?2 AND n.contest = ?3 AND n.id < ?1 AND (n.imageUrl IS NOT NULL OR n.videoUrl IS NOT NULL) AND n.deleted = false ORDER BY n.id DESC")
    Page<Notification> findByGalleryNotificationTypesFromNotificationIdOrderByIdDesc(Long lastNotificationId, List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.notificationType IN ?1 AND n.contest = ?2 AND n.imageUrl IS NOT NULL AND n.videoUrl IS NULL AND n.deleted = false ORDER BY n.id DESC")
    Page<Notification> findByPhotosNotificationTypesOrderByIdDesc(List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.notificationType IN ?2 AND n.contest = ?3 AND n.id < ?1 AND n.imageUrl IS NOT NULL AND n.videoUrl IS NULL AND n.deleted = false ORDER BY n.id DESC")
    Page<Notification> findByPhotosNotificationTypesFromNotificationIdOrderByIdDesc(Long lastNotificationId, List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.notificationType IN ?1 AND n.contest = ?2 AND n.videoUrl IS NOT NULL AND n.deleted = false ORDER BY n.id DESC")
    Page<Notification> findByVideosNotificationTypesOrderByIdDesc(List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.notificationType IN ?2 AND n.contest = ?3 AND n.id < ?1 AND n.videoUrl IS NOT NULL AND n.deleted = false ORDER BY n.id DESC")
    Page<Notification> findByVideosNotificationTypesFromNotificationIdOrderByIdDesc(Long lastNotificationId, List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    List<Notification> findByOffensiveAndContestAndDeletedOrderByIdDesc(boolean offensive, Contest contest, boolean deleted);

    /**
     * Finds notifications, which contain {@code hashtag} and are in the @{code contest}
     */
    @Query("FROM Notification n WHERE UPPER(n.hashtags) LIKE UPPER(?1) AND n.notificationType IN ?2 AND n.contest = ?3 AND n.deleted = false AND n.offensive = false ORDER BY n.id DESC")
    Page<Notification> findByHashtagAndNotificationTypes(String hashtag, List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    /**
     * Finds all notifications which contains both hashtags
     * @param hashtag1 the first hashtag
     * @param hashtag2 the second hashtag
     * @return notifications with hashtags
     */
    @Query(value = "FROM Notification n WHERE n.contest = ?3 AND UPPER(n.hashtags) LIKE UPPER(?1) AND UPPER(n.hashtags) LIKE UPPER(?2) ORDER BY n.id DESC")
    List<Notification> findByHashTagsAndContest(String hashtag1, String hashtag2, Contest contest);

    /**
     * Finds all notifications which contains both hashtags and have ID higher than {@code sinceId}
     * @param hashtag1 the first hashtag
     * @param hashtag2 the second hashtag
     * @param sinceId since ID criteria
     * @return notifications with hashtags and newer than {@code sinceId}
     */
    @Query(value = "FROM Notification n WHERE n.contest = ?4 AND UPPER(n.hashtags) LIKE UPPER(?1) AND UPPER(n.hashtags) LIKE UPPER(?2) AND n.id > ?3 ORDER BY n.id DESC")
    List<Notification> findByHashTagsAndContestSinceId(String hashtag1, String hashtag2, long sinceId, Contest contest);

    List<Notification> findByParentIdAndNotificationTypeAndContest(Long parentId, NotificationType notificationType, Contest contest);

    List<Notification> findByAuthorUsernameAndNotificationTypeAndContest(String username, NotificationType notificationType, Contest contest);

    List<Notification> findByIdIn(List<Long> notificationIds);

    @Query("SELECT n " +
            "FROM Notification n " +
            "WHERE n.featuredEligible = true" +
            "   AND n.deleted = false" +
            "   AND n.contest = ?3" +
            "   AND n.id NOT IN ?2" +
            "   AND (" +
            "           (n.entityId IN (SELECT qc.id FROM QuestChallenge qc WHERE ?1 BETWEEN qc.startDate AND qc.endDate OR (qc.startDate < ?1 AND qc.endDate IS NULL)) AND n.notificationType = 'QUEST_CHALLENGE') OR" +
            "           (n.entityId IN (SELECT p.id FROM Poll p WHERE ?1 BETWEEN p.startDate AND p.endDate) AND n.notificationType = 'POLL_OPEN') OR " +
            "           (n.entityId IN (SELECT an.id FROM AdminNotification an WHERE ?1 BETWEEN an.startDate AND an.endDate) AND n.notificationType = 'ADMIN_NOTIFICATION')" +
            "       ) " +
            "ORDER BY n.timestamp DESC")
    List<Notification> findFeaturedNotifications(Date date, List<Long> ignoredFeatured, Contest contest, Pageable pageable);

    @Query("SELECT COUNT(n) " +
            "FROM Notification n " +
            "WHERE n.featuredEligible = true" +
            "   AND n.deleted = false" +
            "   AND n.contest = ?3" +
            "   AND n.id NOT IN ?2" +
            "   AND (" +
            "           (n.entityId IN (SELECT qc.id FROM QuestChallenge qc WHERE ?1 BETWEEN qc.startDate AND qc.endDate OR (qc.startDate < ?1 AND qc.endDate IS NULL)) AND n.notificationType = 'QUEST_CHALLENGE') OR " +
            "           (n.entityId IN (SELECT p.id FROM Poll p WHERE ?1 BETWEEN p.startDate AND p.endDate) AND n.notificationType = 'POLL_OPEN') OR " +
            "           (n.entityId IN (SELECT an.id FROM AdminNotification an WHERE ?1 BETWEEN an.startDate AND an.endDate) AND n.notificationType = 'ADMIN_NOTIFICATION')" +
            "       ) ")
    Long countFeaturedNotifications(Date date, List<Long> ignoredFeatured, Contest contest);

    @Query("SELECT n " +
            "FROM Notification n " +
            "WHERE n.notificationType IN ?1 " +
            "   AND n.contest = ?4 " +
            "   AND n.deleted = false " +
            "   AND UPPER(n.title) LIKE UPPER(?2) " +
            "   AND UPPER(n.body) LIKE UPPER(?3)")
    Page<Notification> findFilteredNotifications(List<NotificationType> notificationTypes, String title, String body, Contest contest, Pageable pageable);

    @Query("SELECT COUNT(n) " +
            "FROM Notification n " +
            "WHERE n.entityId = ?1" +
            " AND n.body = ?2" +
            " AND n.contest = ?3" +
            " AND (n.notificationType = 'SCOREBOARD_SUCCESS' OR n.notificationType = 'SCOREBOARD_FAILED' OR n.notificationType = 'SCOREBOARD_SUBMIT')")
    long countAnalystSubmissionMessage(Long submissionId, String message, Contest contest);

    @Query("SELECT COUNT(n) " +
            "FROM Notification n " +
            "WHERE  n.entityId = ?1" +
            " AND n.body = ?2" +
            " AND n.contest = ?3" +
            " AND n.notificationType = 'ANALYST_TEAM_MESSAGE'")
    long countAnalystTeamMessage(Long teamId, String message, Contest contest);

    @Query("SELECT COUNT(n) " +
            "FROM Notification n " +
            "WHERE n.entityId IS NULL" +
            " AND n.body = ?1" +
            " AND n.contest = ?2" +
            " AND n.notificationType = 'ANALYST_MESSAGE'")
    long countAnalystGeneralMessage(String message, Contest contest);

    // ---

    List<Notification> findByNotificationType(NotificationType notificationType);

    List<Notification> findByEntityIdAndNotificationType(Long entityId, NotificationType notificationType);

    List<Notification> findByOffensiveOrderByIdDesc(boolean offensive);

    @Query("SELECT n FROM Notification n WHERE n.body LIKE ?1 AND n.body LIKE ?2")
    Page<Notification> findNotificationsByContainingText(String text1, String text2, Pageable pageable);



    @Query("SELECT n FROM Notification n ORDER BY n.id DESC")
    Page<Notification> findAllOrderByIdDesc(Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.id < ?1 ORDER BY n.id DESC")
    Page<Notification> findAllOrderByIdDesc(Long lastId, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.notificationType NOT IN ?1 AND n.offensive = false ORDER BY n.timestamp DESC")
    Page<Notification> findAllExceptListOrderByIdDesc(List<NotificationType> notificationTypes, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.id < ?1 AND n.notificationType NOT IN ?2 AND n.offensive = false ORDER BY n.timestamp DESC")
    Page<Notification> findAllExceptListOrderByIdDesc(Long lastId, List<NotificationType> notificationTypes, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.timestamp < ?1 AND n.notificationType NOT IN ?2 AND n.offensive = false ORDER BY n.timestamp DESC")
    Page<Notification> findAllExceptListOrderByIdDesc(Date timestamp, List<NotificationType> notificationTypes, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.notificationType = ?1 ORDER BY n.id DESC")
    Page<Notification> findNotificationsOrderByIdDesc(NotificationType notificationType1, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE (n.notificationType = ?1 OR n.notificationType = ?2) ORDER BY n.id DESC")
    Page<Notification> findNotificationsOrderByIdDesc(NotificationType notificationType1, NotificationType notificationType2, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE (n.notificationType = ?1 OR n.notificationType = ?2 OR n.notificationType = ?3) ORDER BY n.id DESC")
    Page<Notification> findNotificationsOrderByIdDesc(NotificationType notificationType1, NotificationType notificationType2,
                                                      NotificationType notificationType3, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE (n.notificationType = ?1 OR n.notificationType = ?2) ORDER BY n.id DESC")
    List<Notification> findLatestNotifications(NotificationType notificationType1, NotificationType notificationType2, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.entityId = ?1 AND (n.notificationType = ?2 OR n.notificationType = ?3) ORDER BY n.id DESC")
    List<Notification> findByEntityIdAndNotificationTypeOrderByIdDesc(long entityId, NotificationType notificationType1,
                                                                      NotificationType notificationType2);

    /**
     * @return notifications for polls, which are in progress and not ignored by
     * user
     */
    @Query(value = "SELECT n FROM Notification n WHERE n.entityId IN (SELECT p.id FROM Poll p WHERE ?1 BETWEEN p.startDate AND p.endDate) AND n.id NOT IN ?2 AND n.notificationType = 'POLL_OPEN' AND n.featuredEligible = true")
    List<Notification> findCurrentPollNotifications(Date date, List<Long> ignoredFeatured);

    /**
     * @return notifications for ICPC Notifications, which are in progress and
     * not ignored by user
     */
    @Query(value = "SELECT n FROM Notification n WHERE n.entityId IN (SELECT an.id FROM AdminNotification an WHERE ?1 BETWEEN an.startDate AND an.endDate) AND n.id NOT IN ?2 AND n.notificationType = 'ADMIN_NOTIFICATION' AND n.featuredEligible = true")
    List<Notification> findCurrentAdminNotifications(Date date, List<Long> ignoredFeatured);

    /**
     * @return notifications for Quest challenges, which are in progress and not
     * ignored by user
     */
    @Query(value = "SELECT n FROM Notification n WHERE n.notificationType = 'QUEST_CHALLENGE' AND n.featuredEligible = true AND n.deleted = false AND n.entityId IN (SELECT qc.id FROM QuestChallenge qc WHERE ?1 BETWEEN qc.startDate AND qc.endDate OR (qc.startDate < ?1 AND qc.endDate IS NULL)) AND n.id NOT IN ?2")
    List<Notification> findCurrentQuestChallengeNotifications(Date date, List<Long> ignoredFeatured);

    @Query("SELECT n FROM Notification n WHERE n.id > ?1 AND n.notificationType IN ?2")
    List<Notification> findAllByNotificationTypesAndLastId(Long lastId, List<NotificationType> notificationTypes);

    @Query("SELECT n FROM Notification n WHERE n.entityId IN ?1 AND n.notificationType = ?2")
    List<Notification> findAllByEntityIdsAndNotificationType(List<Long> ids, NotificationType notificationType);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.entityId IN ?1 AND n.notificationType = ?2")
    void deleteAllByEntityIdsAndNotificationType(List<Long> ids, NotificationType notificationType);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.entityId = ?1 AND n.notificationType = ?2 AND n.contest = ?3")
    void deleteByEntityIdAndNotificationType(Long entityId, NotificationType notificationType, Contest contest);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.contest = ?1 AND n.notificationType IN ?2")
    void deleteScoreboardNotificationsByContest(Contest contest, List<NotificationType> notificationTypes);

}
