package com.myicpc.repository.eventFeed;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findByName(String name);

    Region findByNameAndContest(String name, Contest contest);

    @Query("SELECT r FROM Region r ORDER BY r.name ASC")
    List<Region> findAllOrderByNameAsc();

    @Transactional
    @Modifying
    @Query("DELETE FROM Region r WHERE r.contest = ?1")
    void deleteByContest(Contest contest);
}
