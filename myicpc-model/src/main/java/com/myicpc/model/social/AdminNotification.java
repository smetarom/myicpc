package com.myicpc.model.social;

import com.myicpc.model.StartEndDateObject;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

/**
 * Represents an admin notification
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "AdminNotification_id_seq")
public class AdminNotification extends StartEndDateObject {
    private static final long serialVersionUID = -7301869919133771908L;

    /**
     * Notification title
     */
    private String title;
    /**
     * Content of the notification
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String body;
    /**
     * Source URL of the image
     */
    private String imageUrl;
    /**
     * Source URL of the video
     */
    private String videoUrl;

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
}
