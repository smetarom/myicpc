package com.myicpc.repository.teamInfo;

import com.myicpc.model.teamInfo.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findByExternalId(Long externalId);

    Region findByName(String name);

    @Query("SELECT r FROM Region r ORDER BY r.name ASC")
    List<Region> findAllOrderByNameAsc();
}
