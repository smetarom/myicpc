package com.myicpc.model.social;

import com.myicpc.enums.GalleryMediaType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Represents a photo/video from external source
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "GalleryMedia_id_seq")
public class GalleryMedia extends ASocialMedia {
    private static final long serialVersionUID = 4340500841962647768L;

    /**
     * Username of the author
     */
    private String username;
    /**
     * Author full name
     */
    private String displayName;
    /**
     * Author user ID
     */
    private String userId;
    /**
     * Media ID
     */
    private String mediaId;
    /**
     * Description of the media
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String description;
    /**
     * Tag associated with the media
     */
    private String tag;

    /**
     * URL to the source
     */
    private String mediaURL;
    /**
     * URL to the thumbnail
     */
    private String thumbnailURL;
    /**
     * URL to page in the social media
     */
    private String additionalURL;
    /**
     * Profile picture of the author
     */
    private String profilePhotoURL;

    /**
     * Timespamt, when the media was created
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    /**
     * Source type of the media
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private GalleryMediaType galleryMediaType;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(final String mediaId) {
        this.mediaId = mediaId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(final String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(final String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getAdditionalURL() {
        return additionalURL;
    }

    public void setAdditionalURL(final String additionalURL) {
        this.additionalURL = additionalURL;
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }

    public void setProfilePhotoURL(final String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    public GalleryMediaType getGalleryMediaType() {
        return galleryMediaType;
    }

    public void setGalleryMediaType(final GalleryMediaType galleryMediaType) {
        this.galleryMediaType = galleryMediaType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
}
