package com.myicpc.model.quest;

import com.myicpc.commons.utils.TimeUtils;
import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.teamInfo.TeamInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a submission on Techtrek challenge
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "TechTrekSubmission_id_seq")
public class TechTrekSubmission extends IdGeneratedContestObject {
    private static final long serialVersionUID = 1L;

    /**
     * List of respondant's twitter usernames separated by comma
     */
    private String twitterUsernames;

    /**
     * Timestamp of the last change
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedWhen;

    /**
     * Respondend's team
     */
    @ManyToOne
    @JoinColumn(name = "teamInfoId")
    private TeamInfo teamInfo;

    /**
     * List of answers
     */
    @OneToMany(mappedBy = "techTrekSubmission", cascade = CascadeType.ALL)
    private List<TechTrekWriteup> writeups = new ArrayList<TechTrekWriteup>();

    public Date getModifiedWhen() {
        return modifiedWhen;
    }

    /**
     * @return local time, when the submission was submitted
     */
    @Transient
    public Date getLocalModifiedWhen() {
        return TimeUtils.convertUTCDateToLocal(getModifiedWhen(), contest.getContestSettings().getTimeDifference());
    }

    public void setModifiedWhen(Date modifiedWhen) {
        this.modifiedWhen = modifiedWhen;
    }

    public String getTwitterUsernames() {
        return twitterUsernames;
    }

    public void setTwitterUsernames(final String twitterUsernames) {
        this.twitterUsernames = twitterUsernames;
    }

    public TeamInfo getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(final TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }

    public List<TechTrekWriteup> getWriteups() {
        return writeups;
    }

    public void setWriteups(final List<TechTrekWriteup> writeups) {
        this.writeups = writeups;
    }

    /**
     * Update timestamp on save or update
     */
    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        modifiedWhen = new Date();
    }
}
