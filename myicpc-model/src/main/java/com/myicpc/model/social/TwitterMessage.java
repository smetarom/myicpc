package com.myicpc.model.social;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a tweet
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "TwitterMessage_id_seq")
public class TwitterMessage extends ASocialMedia {
    private static final long serialVersionUID = -7498100740146606365L;
    /**
     * URL beginning of Vine URL
     */
    public static final String VINE_START_URL = "https://vine.co/v/";
    /**
     * Tweet id
     */
    @Column(unique = true)
    private Long tweetId;
    /**
     * Id of the original tweet,from which this tweet was retweeted
     */
    private Long retweetedId;
    /**
     * Creation timestamp
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    /**
     * Message content
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(length = 512)
    private String description;
    /**
     * Tweet author username
     */
    private String username;
    /**
     * Tweet author full name
     */
    private String userFullName;
    /**
     * Tweet author profile picture
     */
    private String profileImageUrl;
    /**
     * Tweet hashtags separated by |
     */
    private String hashtags;
    /**
     * Image URL attached to the feed
     */
    private String mediaURL;

    /**
     * Default constructor.
     */
    public TwitterMessage() {
        super();
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(final String text) {
        this.description = text;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(final String userFullName) {
        this.userFullName = userFullName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(final String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(final Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(final String hashtags) {
        this.hashtags = hashtags;
    }

    @Override
    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(final String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public Long getRetweetedId() {
        return retweetedId;
    }

    public void setRetweetedId(final Long retweetedId) {
        this.retweetedId = retweetedId;
    }

    /**
     * @return is the tweet retweeted
     */
    @Transient
    public boolean isRetweet() {
        return retweetedId != null;
    }

    /**
     * @return is the media URL from Vine
     */
    @Transient
    public boolean isVineUrl() {
        if (StringUtils.isEmpty(mediaURL)) {
            return false;
        }
        return mediaURL.startsWith(VINE_START_URL);
    }
}
