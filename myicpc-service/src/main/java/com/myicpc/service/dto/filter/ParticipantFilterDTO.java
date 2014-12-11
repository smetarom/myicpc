package com.myicpc.service.dto.filter;

import com.myicpc.enums.ContestParticipantRole;
import com.myicpc.model.teamInfo.TeamInfo;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class ParticipantFilterDTO implements Serializable {
    private ContestParticipantRole contestParticipantRole;
    private TeamInfo teamInfo;
    private String searchText;

    public ContestParticipantRole getContestParticipantRole() {
        return contestParticipantRole;
    }

    public void setContestParticipantRole(ContestParticipantRole contestParticipantRole) {
        this.contestParticipantRole = contestParticipantRole;
    }

    public TeamInfo getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
