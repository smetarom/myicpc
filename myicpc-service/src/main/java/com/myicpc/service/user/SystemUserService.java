package com.myicpc.service.user;

import au.com.bytecode.opencsv.CSVReader;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.SystemUserRole;
import com.myicpc.repository.security.SystemUserRepository;
import com.myicpc.repository.security.SystemUserRoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
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
    public SystemUser mergeUser(SystemUser user) {
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
        return user;
    }

    /**
     * Imports CSV file with user accounts to the system
     *
     * Columns: username, plain password, firstname, lastname,
     * enabled(true/false), list of roles separated by semicolon
     *
     * @param usersFile
     *            CSV file with user accounts
     * @throws IOException
     *             error during the reading CSV file
     */
    @Transactional
    public void importUsers(final MultipartFile usersFile) throws IOException {
        String[] line;
        try (InputStream fileInputStream = usersFile.getInputStream();
             CSVReader usersReader = new CSVReader(new InputStreamReader(fileInputStream, FormatUtils.DEFAULT_ENCODING))) {
            while ((line = usersReader.readNext()) != null) {
                SystemUser user = systemUserRepository.findByUsername(line[0]);
                if (user == null) {
                    user = new SystemUser();
                }

                if (!line[0]
                        .matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$")) {
                    throw new ValidationException("Username '" + line[0] + "' is not valid email.");
                }
                user.setUsername(line[0]);
                user.setPassword(this.hashPassword(line[1]));
                user.setFirstname(line[2]);
                user.setLastname(line[3]);
                user.setEnabled("true".equalsIgnoreCase(line[4]));

                systemUserRepository.save(user);

                String[] roles = line[5].split(";");
                saveUserRoles(Arrays.asList(roles), user);
            }
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
