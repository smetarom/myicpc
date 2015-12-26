package com.myicpc.dto.eventFeed.parser;

import com.myicpc.dto.eventFeed.convertor.TimestampConverter;
import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.TeamProblem;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.util.Date;

/**
 * @author Roman Smetana
 */
@XStreamAlias("run")
public class TeamProblemXML extends XMLEntity<TeamProblem> {
    @XStreamAlias("id")
    private Long systemId;
    private boolean solved;
    private boolean penalty;
    private Integer attempts;
    private Double time;
    @XStreamConverter(TimestampConverter.class)
    private Date timestamp;
    private boolean judged;
    private String language;
    private String result;
    private String status;
    @XStreamAlias("team")
    private Long teamId;
    @XStreamAlias("problem")
    private Long problemId;

    /**
     * Order number the submission is in the event feed
     */
    private int submissionOrder;

    @Override
    public void mergeTo(final TeamProblem teamProblem) {
        teamProblem.setSystemId(getSystemId());
        teamProblem.setSolved(isSolved());
        teamProblem.setPenalty(isPenalty());
        teamProblem.setAttempts(getAttempts());
        teamProblem.setTime(getTime());
        teamProblem.setTimestamp(getTimestamp());
        teamProblem.setJudged(isJudged());
        teamProblem.setLanguage(getLanguage());
        teamProblem.setResultCode(getResult());
        teamProblem.setStatus(getStatus());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        visitor.visit(this, contest, eventFeedSettings);
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public boolean isPenalty() {
        return penalty;
    }

    public void setPenalty(boolean penalty) {
        this.penalty = penalty;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isJudged() {
        return judged;
    }

    public void setJudged(boolean judged) {
        this.judged = judged;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public int getSubmissionOrder() {
        return submissionOrder;
    }

    public void setSubmissionOrder(int submissionOrder) {
        this.submissionOrder = submissionOrder;
    }
}
