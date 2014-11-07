package com.myicpc.model.security;

import com.myicpc.model.IdGeneratedObject;
import com.myicpc.validator.annotation.ValidateSystemUser;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a system user for security framework
 *
 * @author Roman Smetana
 */
@ValidateSystemUser
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "User_id_seq")
public class SystemUser extends IdGeneratedObject {
    private static final long serialVersionUID = 1203338022775926192L;

    /**
     * List of system roles
     *
     * @author Roman Smetana
     */
    public enum UserRoleEnum {
        ROLE_ADMIN("Admin"), ROLE_MANAGER("Manager"), ROLE_CONTEST("Contest"), ROLE_GALLERY("Gallery"), ROLE_SCOREBOARD("Scoreboard"), ROLE_POLLS(
                "Polls"), ROLE_RSS("RSS"), ROLE_SCHEDULE("Schedule"), ROLE_QUEST("Quest"), ROLE_NOTIFICATION("Notifications"), ROLE_USER("Basic user");

        /**
         * Human readable name of the role
         */
        private String label;

        private UserRoleEnum(final String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    /**
     * Email as login username
     */
    @Email
    @NotBlank
    @Column(unique = true)
    private String username;
    /**
     * Login password hashed
     */
    @Size(min = 6, message = "Password must be longer than 6 characters")
    private String password;
    /**
     * User firstname
     */
    private String firstname;
    /**
     * User last name
     */
    private String lastname;
    /**
     * Is user enabled
     */
    private boolean enabled;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SystemUserRole> roles;

    /**
     * Copy of password
     */
    @Transient
    private String passwordCheck;
    /**
     * Old password hashed
     */
    @Transient
    private String oldPassword;
    /**
     * Old password in plain text
     */
    @Transient
    private String oldPlainPassword;

    /**
     * List of user roles in the system
     */
    @Transient
    private List<String> stringRoles = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public List<SystemUserRole> getRoles() {
        return roles;
    }

    public void setRoles(final List<SystemUserRole> roles) {
        this.roles = roles;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(final String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(final String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getOldPlainPassword() {
        return oldPlainPassword;
    }

    public void setOldPlainPassword(final String oldPlainPassword) {
        this.oldPlainPassword = oldPlainPassword;
    }

    public List<String> getStringRoles() {
        return stringRoles;
    }

    public void setStringRoles(final List<String> stringRoles) {
        this.stringRoles = stringRoles;
    }

    /**
     * @return user name in format "lastname firstname"
     */
    @Transient
    public String getFullname() {
        return lastname + " " + firstname;
    }
}
