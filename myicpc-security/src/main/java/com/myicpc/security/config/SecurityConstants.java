package com.myicpc.security.config;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Security related constants
 *
 * @author Roman Smetana
 */
public class SecurityConstants {
    /**
     * Permissions to apply on the entity
     */
    public enum Permission {
        READ, WRITE;

        /**
         * Translates permission in string into {@link com.myicpc.security.config.SecurityConstants.Permission}
         *
         * @param object permission in string
         * @return {@link com.myicpc.security.config.SecurityConstants.Permission} object or {@code null} if it is not valid permission
         */
        public static Permission toPermission(Object object) {
            if (object != null && object instanceof String) {
                try {
                    String name = (String) object;
                    return Permission.valueOf(name.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * Logged user has 'read' permission on entity or is admin
     *
     * It is used with {@link PostFilter} to filter out of the results, where the logged user does not have permission
     */
    public static final String FILTER_CONTEST_READ_ACCESS_OR_ADMIN = "hasPermission(filterObject, 'read') or hasRole('ROLE_ADMIN')";

    /**
     * Logged user has 'read' permission on entity or is admin
     *
     * It is used with {@link PostAuthorize} to throw exception, if the logged user does not have permission on returned object
     */
    public static final String RETURN_CONTEST_READ_ACCESS_OR_ADMIN = "hasPermission(returnObject, 'read') or hasRole('ROLE_ADMIN')";

    /**
     * Logged user has 'read' permission on entity or is admin
     *
     * It is used with {@link PreAuthorize} to throw exception, if the logged user does not have permission on entity
     *
     * @param entity parameter name of entity
     */
    public static String CONTEST_READ_ACCESS_OR_ADMIN(String entity) {
        return "hasPermission(#" + entity + ", 'read') or hasRole('ROLE_ADMIN')";
    }
}
