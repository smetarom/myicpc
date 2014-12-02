package com.myicpc.model.eventFeed;

import com.myicpc.model.IdGeneratedObject;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "TeamRankHistory_id_seq")
public class TeamRankHistory extends IdGeneratedObject {
    private Integer fromRank;
    private Integer rank;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();
    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;

    public TeamRankHistory() {
    }

    public TeamRankHistory(Team team, Integer rank, Integer fromRank) {
        this.team = team;
        this.rank = rank;
        this.fromRank = fromRank;
    }

    public Integer getFromRank() {
        return fromRank;
    }

    public void setFromRank(Integer fromRank) {
        this.fromRank = fromRank;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
