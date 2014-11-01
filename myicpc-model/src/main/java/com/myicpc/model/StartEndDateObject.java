package com.myicpc.model;

import com.myicpc.commons.utils.TimeUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass
public abstract class StartEndDateObject extends IdGeneratedContestObject {
    private static final long serialVersionUID = -2798312733225642648L;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd H:mm:ss")
    protected Date startDate;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd H:mm:ss")
    protected Date endDate;

    /**
     * If the entity is already published
     */
    protected boolean published;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    @Transient
    public Date getLocalStartDate() {
        return TimeUtils.convertUTCDateToLocal(getStartDate(), contest.getContestSettings().getTimeDifference());
    }

    @Transient
    public void setLocalStartDate(final Date startDate) {
        setStartDate(TimeUtils.convertLocalDateToUTC(startDate, contest.getContestSettings().getTimeDifference()));
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    @Transient
    public Date getLocalEndDate() {
        return TimeUtils.convertUTCDateToLocal(getEndDate(), contest.getContestSettings().getTimeDifference());
    }

    @Transient
    public void setLocalEndDate(final Date endDate) {
        setStartDate(TimeUtils.convertLocalDateToUTC(endDate, contest.getContestSettings().getTimeDifference()));
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
