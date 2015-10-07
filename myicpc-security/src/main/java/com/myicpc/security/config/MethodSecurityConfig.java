package com.myicpc.security.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Defines the method level security configuration
 *
 * @author Roman Smetana
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new MyICPCPermissionEvaluator());
        expressionHandler.setRoleHierarchy(createRoleHierarchy());
        return expressionHandler;
    }

    private RoleHierarchy createRoleHierarchy() {
        String[] relationship = new String[] {
                "ROLE_ADMIN > ROLE_MANAGER",
                "ROLE_MANAGER > ROLE_USER"
        };
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(StringUtils.join(relationship, " "));
        return roleHierarchy;
    }
}
