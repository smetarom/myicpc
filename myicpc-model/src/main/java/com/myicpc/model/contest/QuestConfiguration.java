package com.myicpc.model.contest;

import com.myicpc.commons.utils.TimeUtils;
import com.myicpc.model.IdGeneratedObject;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

@Cacheable
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "QuestConfiguration_id_seq")
public class QuestConfiguration extends IdGeneratedObject {
    private static final long serialVersionUID = 2888711611800779397L;

    private int pointsForVote;
    private String hashtagPrefix;
    private String instructionUrl;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd H:mm:ss")
    private Date deadline;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "mapConfiguration")
    private Contest contest;

    public int getPointsForVote() {
        return pointsForVote;
    }

    public void setPointsForVote(int pointsForVote) {
        this.pointsForVote = pointsForVote;
    }

    public String getHashtagPrefix() {
        return hashtagPrefix;
    }

    public void setHashtagPrefix(String hashtagPrefix) {
        this.hashtagPrefix = hashtagPrefix;
    }

    public String getInstructionUrl() {
        return instructionUrl;
    }

    public void setInstructionUrl(String instructionUrl) {
        this.instructionUrl = instructionUrl;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(final Date deadline) {
        this.deadline = deadline;
    }

    /**
     * @return date in local time
     */
    @Transient
    public Date getLocalDeadline() {
        return TimeUtils.convertUTCDateToLocal(getDeadline(), contest.getTimeDifference());
    }

    /**
     * @param date date in local time
     */
    @Transient
    public void setLocalDeadline(final Date date) {
        setDeadline(TimeUtils.convertLocalDateToUTC(date, contest.getTimeDifference()));
    }

}
