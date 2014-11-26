package com.myicpc.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

/**
 * Application variables stored in database
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Globals_id_seq")
public class Globals extends IdGeneratedObject {
    public enum GlobalsColumn {
        ADMIN_EMAIL(false), FB_API_KEY(false), GOOGLE_NON_AUTHENTICATED_KEY(false), GOOGLE_ANALYTICS_KEY(false), DEFAULT_MAP_CONFIG(true),
        UNIVERSITY_LOGOS_URL(false), TEAM_PICTURES_URL(false), CONTEST_MANAGEMENT_SYSTEM_URL(false);

        private boolean longText;

        GlobalsColumn(boolean longText) {
            this.longText = longText;
        }

        public boolean isLongText() {
            return longText;
        }
    }

    @NotBlank
    @Column(unique = true)
    private String name;
    private String value;
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String text;

    public Globals() {
    }

    public Globals(GlobalsColumn name) {
        this.name = name.toString();
    }

    public Globals(GlobalsColumn name, String value) {
        this.name = name.toString();
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
