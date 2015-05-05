package com.myicpc.dto.eventFeed;

import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;

import java.util.List;

/**
 * @author Roman Smetana
 */
public class EventFeedSubmission implements EventFeedEvent {
    private TeamProblem teamSubmission;
    private List<Team> changedTeams;

    public EventFeedSubmission(TeamProblem teamSubmission, List<Team> changedTeams) {
        this.teamSubmission = teamSubmission;
        this.changedTeams = changedTeams;
    }

    public TeamProblem getTeamSubmission() {
        return teamSubmission;
    }

    public List<Team> getChangedTeams() {
        return changedTeams;
    }
}
