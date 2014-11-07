package com.myicpc.service.user;

import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.SystemUserRole;
import com.myicpc.repository.security.SystemUserRepository;
import com.myicpc.repository.security.SystemUserRoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class SystemUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private SystemUserRoleRepository userRoleRepository;

    /**
     * Verifies if the password match the password check
     * @param systemUser
     * @return
     */
    public boolean checkPasswordVerification(SystemUser systemUser) {
        if (systemUser == null || systemUser.getPassword() == null) {
            return false;
        }
        return systemUser.getPassword().equals(systemUser.getPasswordCheck());
    }

    /**
     * Hashes plain password with the encoder into hashed password
     *
     * @param password
     *            plain password
     * @return hashed password
     */
    public String hashPassword(final String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Updates {@link SystemUser}
     *
     * @param user
     *            user
     */
    public void mergeUser(SystemUser user) {
        List<String> stringRoles = user.getStringRoles();
        user = systemUserRepository.save(user);

        if (stringRoles != null && !stringRoles.isEmpty()) {
            if (user.getRoles() != null) {
                for (SystemUserRole role : user.getRoles()) {
                    userRoleRepository.delete(role);
                }
                user.getRoles().clear();
                systemUserRepository.save(user);
                systemUserRepository.flush();
                user = systemUserRepository.findOne(user.getId());
            }
            saveUserRoles(stringRoles, user);
        }
    }

    protected void saveUserRoles(List<String> roles, SystemUser user) {
        for (String role : roles) {
            if (!StringUtils.isEmpty(role)) {
                SystemUserRole systemUserRole = new SystemUserRole();
                systemUserRole.setAuthority(role);
                systemUserRole.setUser(user);
                userRoleRepository.save(systemUserRole);
            }
        }
    }
}
