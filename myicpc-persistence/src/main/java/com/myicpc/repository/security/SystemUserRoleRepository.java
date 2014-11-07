package com.myicpc.repository.security;

import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.SystemUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SystemUserRoleRepository extends JpaRepository<SystemUserRole, Long> {
	SystemUserRole findByUserAndAuthority(SystemUser user, String authority);

    @Query("SELECT COUNT(ur) FROM SystemUserRole ur WHERE ur.authority = 'ADMIN'")
    Long countAdminUsers();
}
