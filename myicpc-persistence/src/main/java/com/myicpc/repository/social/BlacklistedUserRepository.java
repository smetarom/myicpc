package com.myicpc.repository.social;

import com.myicpc.model.social.BlacklistedUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlacklistedUserRepository extends CrudRepository<BlacklistedUser, Long> {
    @Query("SELECT bu FROM BlacklistedUser bu ORDER BY bu.username, bu.blacklistedUserType")
    List<BlacklistedUser> findAllOrderByUsername();

    List<BlacklistedUser> findByBlacklistedUserType(BlacklistedUser.BlacklistedUserType blacklistType);

    BlacklistedUser findByUsernameAndBlacklistedUserType(String username, BlacklistedUser.BlacklistedUserType blacklistType);

    @Query("SELECT b.username FROM BlacklistedUser b WHERE b.blacklistedUserType = ?1")
    List<String> getUsernameByBlacklistedUserType(BlacklistedUser.BlacklistedUserType blacklistType);
}
