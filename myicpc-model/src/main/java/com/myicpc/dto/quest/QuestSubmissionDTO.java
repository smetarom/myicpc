package com.myicpc.dto.quest;

import com.myicpc.model.quest.QuestSubmission;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class QuestSubmissionDTO implements Serializable {
    private long questParticipantId;
    private long questChallengeId;
    private QuestSubmission.QuestSubmissionState submissionState;
    private String reasonToReject;

    public QuestSubmissionDTO(long questParticipantId, long questChallengeId, QuestSubmission.QuestSubmissionState submissionState, String reasonToReject) {
        this.questParticipantId = questParticipantId;
        this.questChallengeId = questChallengeId;
        this.submissionState = submissionState;
        this.reasonToReject = reasonToReject;
    }

    public long getQuestParticipantId() {
        return questParticipantId;
    }

    public void setQuestParticipantId(long questParticipantId) {
        this.questParticipantId = questParticipantId;
    }

    public long getQuestChallengeId() {
        return questChallengeId;
    }

    public void setQuestChallengeId(long questChallengeId) {
        this.questChallengeId = questChallengeId;
    }

    public QuestSubmission.QuestSubmissionState getSubmissionState() {
        return submissionState;
    }

    public void setSubmissionState(QuestSubmission.QuestSubmissionState submissionState) {
        this.submissionState = submissionState;
    }

    public String getReasonToReject() {
        return reasonToReject;
    }

    public void setReasonToReject(String reasonToReject) {
        this.reasonToReject = reasonToReject;
    }
}
