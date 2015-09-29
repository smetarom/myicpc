package com.myicpc.repository.teamInfo;

import com.myicpc.model.teamInfo.University;
import org.springframework.data.repository.CrudRepository;

public interface UniversityRepository extends CrudRepository<University, Long> {
    University findByExternalUnitId(Long externalId);
}
