package com.myicpc.model.quest;

import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.teamInfo.ContestParticipant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Participant in the quest
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "QuestParticipant_id_seq")
public class QuestParticipant extends IdGeneratedContestObject {
    private static final long serialVersionUID = 997522355338005740L;

    /**
     * Current total number of quest points
     */
    private int points;
    /**
     * Administrator can add/remove points from {@link QuestParticipant}, this
     * changes participant score permanently
     */
    private int pointsAdjustment;

    /**
     * Person who participates in the Quest
     */
    @NotNull
    @OneToOne
    @JoinColumn(name = "contestParticipantId", unique = true)
    private ContestParticipant contestParticipant;

    /**
     * All {@link QuestSubmission} submitted by participant
     */
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    List<QuestSubmission> submissions = new ArrayList<QuestSubmission>();

    public int getPoints() {
        return points;
    }

    public void setPoints(final int points) {
        this.points = points;
    }

    public int getPointsAdjustment() {
        return pointsAdjustment;
    }

    public void setPointsAdjustment(final int pointsAdjustment) {
        this.pointsAdjustment = pointsAdjustment;
    }

    public ContestParticipant getContestParticipant() {
        return contestParticipant;
    }

    public void setContestParticipant(final ContestParticipant contestParticipant) {
        this.contestParticipant = contestParticipant;
    }

    public List<QuestSubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(final List<QuestSubmission> submissions) {
        this.submissions = submissions;
    }

    /**
     * Compute current participant quest points from challenges and point
     * adjustment enter by administrator
     */
    @PrePersist
    @PreUpdate
    public void calcQuestPoints() {
        int points = 0;
        if (submissions != null && !submissions.isEmpty()) {
            for (QuestSubmission submission : submissions) {
                if (submission.isAccepted()) {
                    points += submission.getQuestPoints();
                }
            }
        }
        points += pointsAdjustment;
        this.points = points;
    }

    /**
     * @return number of accepted submissions for participant
     */
    public long getNumSolvedSubmissions() {
        return CollectionUtils.countMatches(submissions, new Predicate<QuestSubmission>() {
            @Override
            public boolean evaluate(QuestSubmission submission) {
                return submission.isAccepted();
            }
        });
    }
}
