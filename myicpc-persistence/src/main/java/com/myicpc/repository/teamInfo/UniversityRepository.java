package com.myicpc.repository.teamInfo;

import com.myicpc.model.teamInfo.University;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UniversityRepository extends CrudRepository<University, Long> {
    University findByExternalId(Long externalId);

    @Query("SELECT u FROM University u ORDER BY u.name")
    List<University> findAllOrderByName();
}
