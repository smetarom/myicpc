package com.myicpc.security.config;

import com.myicpc.model.contest.Contest;
import com.myicpc.security.config.SecurityConstants.Permission;
import com.myicpc.security.dto.LoggedUser;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * Evaluator of security permissions
 *
 * It defines the security restrictions on entities
 *
 * @author Roman Smetana
 */
public class MyICPCPermissionEvaluator implements PermissionEvaluator {
    /**
     * Handles the permission check on {@code targetDomainObject}
     *
     * @param authentication security context
     * @param targetDomainObject object to be tested
     * @param permission required permission
     * @return true if the permission is granted, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject instanceof Contest) {
            return handleContest((Contest) targetDomainObject, (LoggedUser) authentication.getPrincipal(), Permission.toPermission(permission));
        }
        return false;
    }

    /**
     * Not supported in MyICPC
     *
     * @param authentication represents the user in question. Should not be null.
     * @param targetId the identifier for the object instance (usually a Long)
     * @param targetType a String representing the target's type (usually a Java
     * classname). Not null.
     * @param permission a representation of the permission object as supplied by the
     * expression system. Not null.
     * @return true if the permission is granted, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
                                 String targetType, Object permission) {
        return false;
    }

    private boolean handleContest(Contest contest, LoggedUser user, Permission permission) {
        if (permission == null) {
            return false;
        }

        switch (permission) {
            case READ:
            case WRITE:
                return user.hasContest(contest);
        }
        return false;
    }
}
