package com.myicpc.repository.social;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.social.AdminNotification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface AdminNotificationRepository extends CrudRepository<AdminNotification, Long> {
    List<AdminNotification> findByContestOrderByEndDateDesc(Contest contest);

    /**
     * @return All not already published ICPC notifications which has already
     * started
     */
    @Query(value = "SELECT an FROM AdminNotification an WHERE an.published = false AND an.contest = ?2 AND an.startDate < ?1")
    List<AdminNotification> findAllNonpublishedStartedNotifications(Date date, Contest contest);

    @Query("SELECT an FROM AdminNotification an ORDER BY an.endDate ASC")
    List<AdminNotification> findAllOrderByEndDate();

    /**
     * @return Returns all ICPC notifications, which are in the progress
     */
    @Query("SELECT an FROM AdminNotification an WHERE ?1 BETWEEN an.startDate AND an.endDate ORDER BY an.startDate DESC")
    List<AdminNotification> findAllCurrentOrderByStartDateDesc(Date date);

    /**
     * @return All not already published ICPC notifications which has already
     * started
     */
    @Query(value = "SELECT an FROM AdminNotification an WHERE an.published = false AND an.startDate < ?1")
    List<AdminNotification> findAllNonpublishedStartedNotifications(Date date);
}
