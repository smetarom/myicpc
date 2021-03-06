package com.myicpc.dto.quest;

import com.myicpc.enums.SortOrder;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;

import java.io.Serializable;

/**
 * Represents filter for quest submission
 * 
 * @author Roman Smetana
 */
public class QuestSubmissionFilter implements Serializable {
	private static final long serialVersionUID = -4140466990864140810L;

	private SortOrder sortOrder;
	/**
	 * Quest submission state
	 */
	private QuestSubmission.QuestSubmissionState submissionState;
	/**
	 * Quest challenge
	 */
	private QuestChallenge challenge;
	/**
	 * Quest participant
	 */
	private QuestParticipant participant;

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public QuestSubmission.QuestSubmissionState getSubmissionState() {
		return submissionState;
	}

	public void setSubmissionState(final QuestSubmission.QuestSubmissionState submissionState) {
		this.submissionState = submissionState;
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
}
