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
}
