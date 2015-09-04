package com.myicpc.security.config;

import com.myicpc.model.contest.Contest;
import com.myicpc.security.config.SecurityConstants.Permission;
import com.myicpc.security.dto.LoggedUser;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class MyICPCPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject instanceof Contest) {
            return handleContest((Contest) targetDomainObject, (LoggedUser) authentication.getPrincipal(), Permission.toPermission(permission));
        }
        return false;
    }

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
