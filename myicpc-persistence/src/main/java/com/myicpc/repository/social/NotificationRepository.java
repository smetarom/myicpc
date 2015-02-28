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
    List<Notification> findByContestAndEntityIdAndNotificationType(Contest contest, Long entityId, NotificationType notificationType);

    Notification findByContestAndExternalIdAndNotificationType(Contest contest, String externalId, NotificationType notificationType);

    @Query("SELECT n FROM Notification n WHERE n.entityId IN ?1 AND n.notificationType IN ?2")
    List<Notification> findByEntityIdsAndTypes(List<Long> entityIds, List<NotificationType> notificationTypes, Sort sort);

    @Query("SELECT n FROM Notification n WHERE n.notificationType IN ?1 AND n.contest = ?2 ORDER BY n.id DESC")
    Page<Notification> findByNotificationTypesOrderByIdDesc(List<NotificationType> notificationTypes, Contest contest, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.body LIKE ?1 AND n.body LIKE ?2 AND n.contest = ?3")
    Page<Notification> findNotificationsByContainingTextAndContest(String text1, String text2, Contest contest, Pageable pageable);

    // ---

    List<Notification> findByNotificationType(NotificationType notificationType);

    List<Notification> findByEntityIdAndNotificationType(Long entityId, NotificationType notificationType);

    List<Notification> findByOffensiveOrderByIdDesc(boolean offensive);

    @Query("SELECT n FROM Notification n WHERE n.body LIKE ?1 AND n.body LIKE ?2")
    Page<Notification> findNotificationsByContainingText(String text1, String text2, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.notificationType IN ?1 AND n.title LIKE ?2 AND n.body LIKE ?3")
    Page<Notification> findFilteredNotifications(List<NotificationType> notificationTypes, String title, String body, Pageable pageable);

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
    @Query(value = "SELECT n FROM Notification n WHERE n.entityId IN (SELECT qc.id FROM QuestChallenge qc WHERE ?1 BETWEEN qc.startDate AND qc.endDate OR (qc.startDate < ?1 AND qc.endDate IS NULL)) AND n.id NOT IN ?2 AND n.notificationType = 'QUEST_CHALLENGE' AND n.featuredEligible = true")
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
    @Query("DELETE FROM Notification n WHERE n.entityId = ?1 AND n.notificationType = ?2")
    void deleteByEntityIdAndNotificationType(Long entityId, NotificationType notificationType);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.contest = ?1 AND n.notificationType IN ?2")
    void deleteScoreboardNotificationsByContest(Contest contest, List<NotificationType> notificationTypes);
}
