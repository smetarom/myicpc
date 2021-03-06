package com.myicpc.repository.kiosk;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.kiosk.KioskContent;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * DAO repository for {@link KioskContent}
 *
 * @author Roman Smetana
 */
public interface KioskContentRepository extends JpaRepository<KioskContent, Long> {
    List<KioskContent> findByContest(Contest contest);

    List<KioskContent> findByContest(Contest contest, Sort sort);

    List<KioskContent> findByActiveAndContest(boolean active, Contest contest);
}
