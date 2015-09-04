package com.myicpc.security.config;

/**
 * @author Roman Smetana
 */
public class SecurityConstants {
    public enum Permission {
        READ, WRITE;

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

    public static final String FILTER_CONTEST_READ_ACCESS_OR_ADMIN = "hasPermission(filterObject, 'read') or hasRole('ROLE_ADMIN')";
    public static final String RETURN_CONTEST_READ_ACCESS_OR_ADMIN = "hasPermission(returnObject, 'read') or hasRole('ROLE_ADMIN')";

    public static final String CONTEST_READ_ACCESS_OR_ADMIN(String entity) {
        return "hasPermission(#" + entity + ", 'read') or hasRole('ROLE_ADMIN')";
    }
}
