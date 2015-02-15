package com.myicpc.model.quest;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.IdGeneratedObject;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
     * Type of the {@link QuestSubmission}
     *
     * @author Roman Smetana
     */
    public enum QuestSubmissionType {
        INVISIBLE(null), TWITTER(NotificationType.QUEST_VOTE_WINNER_TWITTER), INSTAGRAM_IMAGE(NotificationType.QUEST_VOTE_WINNER_INSTAGRAM_IMAGE), INSTAGRAM_VIDEO(
                NotificationType.QUEST_VOTE_INSTAGRAM_VIDEO), VINE(NotificationType.QUEST_VOTE_WINNER_VINE);

        private NotificationType notificationType;

        private QuestSubmissionType(final NotificationType notificationType) {
            this.notificationType = notificationType;
        }

        public NotificationType getNotificationType() {
            return notificationType;
        }
    }

    /**
     * State of the {@link QuestSubmission}
     *
     * @author Roman Smetana
     */
    public enum QuestSubmissionState {
        ACCEPTED("Accepted"), PENDING("Pending"), REJECTED("Rejected");

        private String label;

        private QuestSubmissionState(final String label) {
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

        private String label;

        private VoteSubmissionState(final String label) {
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
     * Social media ID, from which we received the submission
     */
    private String externalId;

    /**
     * Text of the submission
     */
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String text;

    /**
     * URL of image/video or other media
     */
    private String mediaURL;

    /**
     * Reason why the submission was rejected
     */
    private String reasonToReject;

    /**
     * Create timestamp
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    /**
     * Submission for {@link QuestChallenge}
     */
    @ManyToOne
    @JoinColumn(name = "challengeId")
    private QuestChallenge challenge;

    /**
     * Submitted by {@link QuestParticipant}
     */
    @ManyToOne
    @JoinColumn(name = "participantId")
    private QuestParticipant participant;

    /**
     * Submission state
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestSubmissionState submissionState;

    /**
     * Submission type
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestSubmissionType submissionType;

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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(final String externalId) {
        this.externalId = externalId;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(final String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
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

    public QuestSubmissionType getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(final QuestSubmissionType submissionType) {
        this.submissionType = submissionType;
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

    /**
     * @return submission without HTML tags
     */
    @Transient
    public String getEscapedText() {
        return text.replaceAll("\\<.*?>", "");
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
