package com.myicpc.model.kiosk;

import com.myicpc.model.IdGeneratedContestObject;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

/**
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "KioskContent_id_seq")
public class KioskContent extends IdGeneratedContestObject {
    private boolean active;

    private String name;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String content;

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
