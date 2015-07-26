package com.myicpc.dto.quest;

import com.myicpc.model.quest.QuestSubmission;

import java.io.Serializable;

/**
 * @author Roman Smetana
 */
public class QuestSubmissionDTO implements Serializable {
    private final Long questParticipantId;
    private final Long questChallengeId;
    private final QuestSubmission.QuestSubmissionState submissionState;
    private final String reasonToReject;

    public QuestSubmissionDTO(Long questParticipantId, Long questChallengeId, QuestSubmission.QuestSubmissionState submissionState, String reasonToReject) {
        this.questParticipantId = questParticipantId;
        this.questChallengeId = questChallengeId;
        this.submissionState = submissionState;
        this.reasonToReject = reasonToReject;
    }

    public Long getQuestParticipantId() {
        return questParticipantId;
    }

    public Long getQuestChallengeId() {
        return questChallengeId;
    }


    public QuestSubmission.QuestSubmissionState getSubmissionState() {
        return submissionState;
    }

    public String getReasonToReject() {
        return reasonToReject;
    }

}
