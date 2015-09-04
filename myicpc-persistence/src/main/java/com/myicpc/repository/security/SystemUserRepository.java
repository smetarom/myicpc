package com.myicpc.repository.security;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.security.SystemUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    @Query("SELECT u FROM SystemUser u LEFT JOIN FETCH u.roles WHERE u.id = ?1")
    SystemUser findById(Long id);

    @Query("SELECT u FROM SystemUser u LEFT JOIN FETCH u.roles WHERE u.username = ?1")
    SystemUser findByUsername(String username);

    @Query("SELECT u FROM SystemUser u ORDER BY u.lastname")
    List<SystemUser> findAllOrderByLastname();

    @Query("SELECT DISTINCT u FROM SystemUser u LEFT JOIN FETCH u.roles ORDER BY u.lastname")
    List<SystemUser> findAllWithRolesOrderByLastname();

    @Query("SELECT DISTINCT u FROM SystemUser u LEFT JOIN FETCH u.roles ORDER BY u.username")
    List<SystemUser> findAllWithRolesOrderByUsername();

    @Query("SELECT DISTINCT u " +
            "FROM SystemUser u " +
            "WHERE u NOT IN (SELECT uca.systemUser FROM UserContestAccess uca WHERE uca.contest = ?1) " +
            "   AND u.enabled = TRUE")
    List<SystemUser> findAvailableContestManagers(Contest contest, Sort sort);
}
