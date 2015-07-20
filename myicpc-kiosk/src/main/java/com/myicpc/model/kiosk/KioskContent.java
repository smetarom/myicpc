package com.myicpc.model.kiosk;

import com.myicpc.model.IdGeneratedContestObject;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

/**
 * Represents a HTML custom kiosk page
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "KioskContent_id_seq")
public class KioskContent extends IdGeneratedContestObject {
    /**
     * Kiosk custom page label
     */
    private String name;

    /**
     * HTML content code
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String content;

    /**
     * Is this page displayed on the kiosk custom view
     */
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
