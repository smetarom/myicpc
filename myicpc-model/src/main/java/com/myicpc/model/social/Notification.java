package com.myicpc.model.social;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myicpc.commons.utils.TimeUtils;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.IdGeneratedContestObject;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Notification about system event or about external event
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Notification_id_seq")
public class Notification extends IdGeneratedContestObject {
    private static final Logger logger = LoggerFactory.getLogger(Notification.class);
    private static final long serialVersionUID = 7111388236875951578L;

    /**
     * Short description of the notification
     */
    @Column(length = 1024)
    private String title;
    /**
     * Body of the notification
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String body;
    /**
     * URL related to notification
     */
    @Column(length = 1024)
    private String url;
    /**
     * Source URL of the image
     */
    @Column(length = 1024)
    private String imageUrl;
    /**
     * Source URL of the video
     */
    @Column(length = 1024)
    private String videoUrl;
    /**
     * URL to the thumbnail
     */
    @Column(length = 1024)
    private String thumbnailUrl;
    /**
     * Author username
     */
    private String authorUsername;
    /**
     * Author display name
     */
    private String authorName;
    /**
     * Profile picture of the author
     */
    @Column(length = 1024)
    private String profilePictureUrl;
    /**
     * Hashtags separated by |
     */
    @Column(length = 2048)
    private String hashtags;
    /**
     * Any extra information needed for notification
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String code;
    /**
     * Timestamp of the notification creation
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    /**
     * ID from the social media
     */
    private String externalId;
    /**
     * Id of the original tweet,from which this tweet was retweeted
     */
    private Long parentId;
    /**
     * ID related to the event, which created this notification
     */
    private Long entityId;
    /**
     * The team ID, which is referred in the notification
     */
    private Long teamId;
    /**
     * Does the notification contain swear words?
     */
    private boolean offensive;
    /**
     * Can the notification be promoted to featured notifications
     */
    private boolean featuredEligible = true;
    /**
     * Flag, if the notification is hidden to end user
     */
    private boolean deleted;

    /**
     * Type of the notification
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private NotificationType notificationType;

    @Transient
    private String highlightedBody;
    /**
     * {@link #code} can be represented by JSON, this stores key value pairs
     */
    @Transient
    private Map<String, String> parsedCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(final String authorName) {
        this.authorName = authorName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(final String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        parsedCode = null;
        this.code = code;
    }

    public boolean isOffensive() {
        return offensive;
    }

    public void setOffensive(final boolean offensive) {
        this.offensive = offensive;
    }

    public boolean isFeaturedEligible() {
        return featuredEligible;
    }

    public void setFeaturedEligible(final boolean featuredEligible) {
        this.featuredEligible = featuredEligible;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(final NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(final Long teamId) {
        this.entityId = teamId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long retweetedId) {
        this.parentId = retweetedId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return timestamp in format HH:mm
     */
    @Transient
    public String getTime() {
        return TimeUtils.getTimeFormat().format(timestamp);
    }

    /**
     * @return timestamp in local time
     */
    @Transient
    public Date getLocalTimestamp() {
        return TimeUtils.convertUTCDateToLocal(getTimestamp(), contest.getTimeDifference());
    }

    /**
     * Parses JSON into Map
     *
     * @return JSON object represented as map
     */
    public Map<String, String> getParsedCode() {
        if (parsedCode == null && code != null) {
            parsedCode = new HashMap<>();
            JsonObject obj = new JsonParser().parse(code).getAsJsonObject();
            for (Entry<String, JsonElement> entry : obj.entrySet()) {
                parsedCode.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return parsedCode;
    }

    public String getHighlightedBody() {
        return highlightedBody;
    }

    public void setHighlightedBody(String highlightedBody) {
        this.highlightedBody = highlightedBody;
    }
}
