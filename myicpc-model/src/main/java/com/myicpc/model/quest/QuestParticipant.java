package com.myicpc.model.quest;

import com.myicpc.dto.quest.QuestSubmissionDTO;
import com.myicpc.model.IdGeneratedContestObject;
import com.myicpc.model.teamInfo.ContestParticipant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Participant in the quest
 *
 * @author Roman Smetana
 */
@SuppressWarnings("ALL")
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "QuestParticipant_id_seq")
public class QuestParticipant extends IdGeneratedContestObject {
    private static final long serialVersionUID = 997522355338005740L;

    /**
     * Current total number of quest points
     */
    private int points;
    /**
     * Points gained in submission voting
     */
    private int pointsFromVoting;
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
    private List<QuestSubmission> submissions = new ArrayList<>();

    @Transient
    private int acceptedSubmissions;
    @Transient
    private final Map<Long, QuestSubmissionDTO> submissionMap = new HashMap<>();

    public int getPoints() {
        return points;
    }

    public void setPoints(final int points) {
        this.points = points;
    }

    public int getPointsFromVoting() {
        return pointsFromVoting;
    }

    public void setPointsFromVoting(int pointsFromVoting) {
        this.pointsFromVoting = pointsFromVoting;
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
        this.points = points;
    }

    @Transient
    public int getTotalPoints() {
        return points + pointsAdjustment + pointsFromVoting;
    }

    @Transient
    public void addSubmissionDTO(QuestSubmissionDTO submissionDTO) {
        submissionMap.put(submissionDTO.getQuestChallengeId(), submissionDTO);
    }

    public Map<Long, QuestSubmissionDTO> getSubmissionMap() {
        return submissionMap;
    }

    public int getAcceptedSubmissions() {
        return acceptedSubmissions;
    }

    public void setAcceptedSubmissions(int acceptedSubmissions) {
        this.acceptedSubmissions = acceptedSubmissions;
    }
}
