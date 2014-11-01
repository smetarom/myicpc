package com.myicpc.model.contest;

import com.myicpc.model.IdGeneratedObject;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "QuestConfiguration_id_seq")
public class QuestConfiguration extends IdGeneratedObject {
    private static final long serialVersionUID = 2888711611800779397L;

    private Integer pointsForVote;
    private String hashtagPrefix;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "mapConfiguration")
    private Contest contest;

    public Integer getPointsForVote() {
        return pointsForVote;
    }

    public void setPointsForVote(Integer pointsForVote) {
        this.pointsForVote = pointsForVote;
    }

    public String getHashtagPrefix() {
        return hashtagPrefix;
    }

    public void setHashtagPrefix(String hashtagPrefix) {
        this.hashtagPrefix = hashtagPrefix;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

}
