package com.myicpc.model.security;

import com.myicpc.model.IdGeneratedObject;

import javax.persistence.*;

/**
 * Represents a security role assigned to {@link SystemUser}
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "UserRoles_id_seq")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "authority"})})
public class SystemUserRole extends IdGeneratedObject {
    private static final long serialVersionUID = 447862063476520565L;

    /**
     * User role defined
     */
    private String authority;

    /**
     * User, who has been assigned the role
     */
    @ManyToOne
    @JoinColumn(name = "userId")
    private SystemUser user;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(final String authority) {
        this.authority = authority;
    }

    public SystemUser getUser() {
        return user;
    }

    public void setUser(final SystemUser user) {
        this.user = user;
    }

    public String getAuthorityLabel() {
        try {
            return SystemUser.UserRoleEnum.valueOf(authority).getLabel();
        } catch (IllegalArgumentException | NullPointerException ex) {
            return authority;
        }
    }
}
