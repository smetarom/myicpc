package com.myicpc.model.quest;

import com.myicpc.model.IdGeneratedObject;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Represents an answer on one Techtrek challenge question
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "TechTrekWriteup_id_seq")
public class TechTrekWriteup extends IdGeneratedObject {
    private static final long serialVersionUID = -4630438571567418334L;

    /**
     * Short description
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String description;
    /**
     * Title
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String line;
    /**
     * Long description
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String writeup;
    private int writeupOrder;

    /**
     * List of techtrek submissions
     */
    @ManyToOne
    @JoinColumn(name = "techTrekSubmissionId")
    private TechTrekSubmission techTrekSubmission;

    public int getWriteupOrder() {
        return writeupOrder;
    }

    public void setWriteupOrder(final int writeupOrder) {
        this.writeupOrder = writeupOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLine() {
        return line;
    }

    public void setLine(final String line) {
        this.line = line;
    }

    public String getWriteup() {
        return writeup;
    }

    public void setWriteup(final String writeup) {
        this.writeup = writeup;
    }

    public TechTrekSubmission getTechTrekSubmission() {
        return techTrekSubmission;
    }

    public void setTechTrekSubmission(final TechTrekSubmission techTrekSubmission) {
        this.techTrekSubmission = techTrekSubmission;
    }
}
