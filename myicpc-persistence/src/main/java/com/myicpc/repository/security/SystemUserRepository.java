package com.myicpc.repository.security;

import com.myicpc.model.security.SystemUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SystemUserRepository extends PagingAndSortingRepository<SystemUser, Long> {
    SystemUser findByUsername(String username);

    @Query("SELECT u FROM SystemUser u ORDER BY u.lastname")
    List<SystemUser> findAllOrderByLastname();
}
