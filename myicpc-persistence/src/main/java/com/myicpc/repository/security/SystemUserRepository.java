package com.myicpc.repository.security;

import com.myicpc.model.security.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    @Query("SELECT u FROM SystemUser u JOIN FETCH u.roles WHERE u.id = ?1")
    SystemUser findById(Long id);

    @Query("SELECT u FROM SystemUser u JOIN FETCH u.roles WHERE u.username = ?1")
    SystemUser findByUsername(String username);

    @Query("SELECT u FROM SystemUser u ORDER BY u.lastname")
    List<SystemUser> findAllOrderByLastname();

    @Query("SELECT u FROM SystemUser u LEFT JOIN FETCH u.roles ORDER BY u.lastname")
    List<SystemUser> findAllWithRolesOrderByLastname();

}
