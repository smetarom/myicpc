package com.myicpc.model.quest;

import com.myicpc.model.IdGeneratedObject;
import com.myicpc.model.social.Notification;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * Quest submission, which tries to solve the {@link QuestChallenge}
 *
 * @author Roman Smetana
 */
@Entity
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "idgen", sequenceName = "QuestSubmission_id_seq")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"challengeId", "participantId"}))
public class QuestSubmission extends IdGeneratedObject {
    private static final long serialVersionUID = 3688412990863522876L;

    /**
     * State of the {@link QuestSubmission}
     *
     * @author Roman Smetana
     */
    public enum QuestSubmissionState {
        ACCEPTED("Accepted"), PENDING("Pending"), REJECTED("Rejected");

        private final String label;

        QuestSubmissionState(final String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    /**
     * State of the {@link QuestSubmission} in the voting system
     *
     * @author Roman Smetana
     */
    public enum VoteSubmissionState {
        IN_PROGRESS("In progress"), VOTE_WINNER("Vote winner");

        private final String label;

        VoteSubmissionState(final String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    /**
     * Number of point rewarded
     */
    private int questPoints = 0;

    /**
     * Number of votes in the running voting round
     */
    private int votes = 0;

    /**
     * Reason why the submission was rejected
     */
    private String reasonToReject;

    /**
     * Submission for {@link QuestChallenge}
     */
    @ManyToOne
    @NotNull
    @JoinColumn(name = "challengeId")
    private QuestChallenge challenge;

    /**
     * Submitted by {@link QuestParticipant}
     */
    @ManyToOne
    @NotNull
    @JoinColumn(name = "participantId")
    private QuestParticipant participant;


    /**
     * {@link com.myicpc.model.social.Notification}, from which we received the submission
     */
    @ManyToOne
    @JoinColumn(name = "notificationId")
    private Notification notification;

    /**
     * Submission state
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestSubmissionState submissionState;

    /**
     * Submission state in the voting system
     */
    @Enumerated(EnumType.STRING)
    private VoteSubmissionState voteSubmissionState;

    public int getQuestPoints() {
        return questPoints;
    }

    public void setQuestPoints(final int questPoints) {
        this.questPoints = questPoints;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(final int votePoints) {
        this.votes = votePoints;
    }


    public QuestChallenge getChallenge() {
        return challenge;
    }

    public void setChallenge(final QuestChallenge challenge) {
        this.challenge = challenge;
    }

    public QuestParticipant getParticipant() {
        return participant;
    }

    public void setParticipant(final QuestParticipant participant) {
        this.participant = participant;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getReasonToReject() {
        return reasonToReject;
    }

    public void setReasonToReject(final String reasonToReject) {
        this.reasonToReject = reasonToReject;
    }

    public QuestSubmissionState getSubmissionState() {
        return submissionState;
    }

    public void setSubmissionState(final QuestSubmissionState submissionState) {
        this.submissionState = submissionState;
    }

    public VoteSubmissionState getVoteSubmissionState() {
        return voteSubmissionState;
    }

    public void setVoteSubmissionState(final VoteSubmissionState voteSubmissionState) {
        this.voteSubmissionState = voteSubmissionState;
    }

    /**
     * Call {@link QuestParticipant#calcQuestPoints()} on submission save or
     * update
     */
    @PrePersist
    @PreUpdate
    public void calcVotePoints() {
        if (participant != null) {
            participant.calcQuestPoints();
        }
    }

    @Transient
    public boolean isAccepted() {
        return submissionState == QuestSubmissionState.ACCEPTED;
    }

    @Transient
    public boolean isRejected() {
        return submissionState == QuestSubmissionState.REJECTED;
    }

    @Transient
    public boolean isPending() {
        return submissionState == QuestSubmissionState.PENDING;
    }
}
