package com.myicpc.service.scoreboard.dto;

import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.social.Notification;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
//TODO rename
public class SubmissionDTO implements Serializable {
    private static final long serialVersionUID = 9132784638908071165L;

    private TeamProblem teamProblem;
    private Notification notification;
    private int contestTime;

    public SubmissionDTO(Notification notification) {
        this.notification = notification;
    }

    public TeamProblem getTeamProblem() {
        return teamProblem;
    }

    public void setTeamProblem(TeamProblem teamProblem) {
        this.teamProblem = teamProblem;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public int getContestTime() {
        return contestTime;
    }

    public void setContestTime(int contestTime) {
        this.contestTime = contestTime;
    }
}
