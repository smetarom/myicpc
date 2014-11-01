package com.myicpc.model.quest;

import com.myicpc.model.StartEndDateObject;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * A Challenge is a single task within Quest. Each challenge tells participants
 * what to do to solve it
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "QuestChallange_id_seq")
public class QuestChallenge extends StartEndDateObject {
    private static final long serialVersionUID = -7014437159443310905L;

    /**
     * Challenge name
     */
    private String name;

    /**
     * Task description
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String description;

    /**
     * Default number of points for solution to this challenge
     * <p/>
     * Admin can change it, before he approves a {@link QuestSubmission}
     */
    private int defaultPoints;

    /**
     * URL of the image, which illustrates the challenge topic
     */
    private String imageURL;

    /**
     * Every challenge has an unique hashtag.
     * <p/>
     * This hashtag consists of prefix (quest.hashtagPrefix in
     * settings.properties) common to all quest challenges and hashtag suffix,
     * which is unique to each challenge
     */
    @Column(unique = true)
    @NotNull
    @NotEmpty
    private String hashtagSuffix;

    /**
     * List of all submission to challenge
     */
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<QuestSubmission> submissions;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public int getDefaultPoints() {
        return defaultPoints;
    }

    public void setDefaultPoints(final int defaultPoints) {
        this.defaultPoints = defaultPoints;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(final String imageURL) {
        this.imageURL = imageURL;
    }

    public String getHashtagSuffix() {
        return hashtagSuffix;
    }

    public void setHashtagSuffix(final String hashtagSuffix) {
        this.hashtagSuffix = hashtagSuffix;
    }

    /**
     * @return full challenge hashtag
     */
    @Transient
    public String getHashtag() {
        return contest.getQuestConfiguration().getHashtagPrefix() + hashtagSuffix;
    }

    public List<QuestSubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(final List<QuestSubmission> submissions) {
        this.submissions = submissions;
    }

    /**
     * @return difference between now and endDate in seconds
     */
    @Transient
    public long getSecondsToEndDate() {
        if (endDate == null) {
            return 0;
        }
        Double timediff = (endDate.getTime() - new Date().getTime()) / 1000.0;
        return timediff > 0 ? timediff.longValue() : 0;
    }

    /**
     * @return description, which will be showed in Quest notification
     */
    @Transient
    public String getNotificationDescription() {
        return description;
    }
}
