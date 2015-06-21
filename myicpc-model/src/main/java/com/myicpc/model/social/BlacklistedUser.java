package com.myicpc.model.social;

import com.myicpc.model.IdGeneratedContestObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Blacklist of users who violated some rule for given service
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "SocialMediaUser_id_seq")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "blacklistedUserType"})})
public class BlacklistedUser extends IdGeneratedContestObject {
    private static final long serialVersionUID = 6111410017922277873L;

    public enum BlacklistedUserType {
        /**
         * Tweets and other twitter activity
         */
        TWITTER("Twitter"), YOUTUBE("Youtube"), VINE("Vine"), INSTAGRAM("Instagram"), QUEST("Quest");
        private final String label;

        BlacklistedUserType(final String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public boolean isTwitter() {
            return this == TWITTER;
        }

        public boolean isYoutube() {
            return this == YOUTUBE;
        }

        public boolean isVine() {
            return this == VINE;
        }

        public boolean isInstagram() {
            return this == INSTAGRAM;
        }

        public boolean isQuest() {
            return this == QUEST;
        }
    }

    /**
     * Username of the user who violated rules
     */
    @NotNull
    private String username;

    /**
     * Type of service
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private BlacklistedUserType blacklistedUserType;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public BlacklistedUserType getSocialMediaUserType() {
        return blacklistedUserType;
    }

    public void setSocialMediaUserType(final BlacklistedUserType blacklistedUserType) {
        this.blacklistedUserType = blacklistedUserType;
    }
}
