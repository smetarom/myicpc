package com.myicpc.repository;

import com.myicpc.model.ExampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Roman Smetana
 */
public interface ExampleRepository extends JpaRepository<ExampleEntity, Long> {
}
