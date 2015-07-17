package com.myicpc.repository.kiosk;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.kiosk.KioskContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Roman Smetana
 */
public interface KioskContentRepository extends JpaRepository<KioskContent, Long> {
    List<KioskContent> findByContest(Contest contest);

    List<KioskContent> findByActive(boolean active);
}
