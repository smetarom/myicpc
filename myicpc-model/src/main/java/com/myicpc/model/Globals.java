package com.myicpc.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

/**
 * Application variables stored in database
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "Globals_id_seq")
public class Globals extends IdGeneratedObject {
    private static final long serialVersionUID = -7918646417534688826L;

    public enum GlobalsColumn {
        ADMIN_EMAIL,
        FB_API_KEY,
        GOOGLE_NON_AUTHENTICATED_KEY,
        GOOGLE_ANALYTICS_KEY,
        DEFAULT_MAP_CONFIG(true),
        UNIVERSITY_LOGOS_URL,
        TEAM_PICTURES_URL,
        CONTEST_MANAGEMENT_SYSTEM_URL,
        CALLBACK_URL,
        SMTP_HOST,
        SMTP_PORT,
        SMTP_USERNAME,
        SMTP_PASSWORD
        ;

        private boolean longText;

        GlobalsColumn() { }

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
