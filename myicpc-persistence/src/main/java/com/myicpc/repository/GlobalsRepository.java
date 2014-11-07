package com.myicpc.repository;

import com.myicpc.model.Globals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Roman Smetana
 */
public interface GlobalsRepository extends JpaRepository<Globals, Long> {
    Globals findByName(String name);

    @Query("SELECT g.value FROM Globals g WHERE g.name = ?1")
    String findValueByName(String name);
}
