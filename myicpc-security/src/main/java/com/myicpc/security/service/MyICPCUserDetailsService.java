package com.myicpc.security.service;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.SystemUserRole;
import com.myicpc.model.security.UserContestAccess;
import com.myicpc.repository.security.SystemUserRepository;
import com.myicpc.repository.security.UserContestAccessRepository;
import com.myicpc.security.dto.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service responsible for user security
 *
 * It is responsible for logging in users and put them into Spring security context
 *
 * @author Roman Smetana
 */
@Service("userDetailsService")
public class MyICPCUserDetailsService implements UserDetailsService {

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private UserContestAccessRepository userContestAccessRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser systemUser = systemUserRepository.findByUsername(username);
        Set<Contest> contests = new HashSet<>();
        if (systemUser == null) {
            throw new UsernameNotFoundException("Username not found.");
        }

        List<UserContestAccess> contestAccesses = userContestAccessRepository.findBySystemUser(systemUser);
        for (UserContestAccess contestAccess : contestAccesses) {
            contests.add(contestAccess.getContest());
        }
        return new LoggedUser(systemUser, buildUserAuthority(systemUser.getRoles()), contests);
    }

    private List<GrantedAuthority> buildUserAuthority(List<SystemUserRole> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<>();

        // Build user's authorities
        for (SystemUserRole userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getAuthority()));
        }

        return new ArrayList<>(setAuths);
    }
}
