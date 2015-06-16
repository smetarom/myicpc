package com.myicpc.service.quest;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.quest.QuestChallenge;
import com.myicpc.model.quest.QuestParticipant;
import com.myicpc.model.quest.QuestSubmission;
import com.myicpc.model.quest.QuestSubmission.QuestSubmissionState;
import com.myicpc.model.quest.QuestSubmission.VoteSubmissionState;
import com.myicpc.model.social.Notification;
import com.myicpc.model.teamInfo.ContestParticipant;
import com.myicpc.repository.quest.QuestChallengeRepository;
import com.myicpc.repository.quest.QuestParticipantRepository;
import com.myicpc.repository.quest.QuestSubmissionRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.repository.teamInfo.ContestParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class QuestSubmissionService {
    public static final int VOTE_ROUND_SIZE = 4;
    public static final int MIN_VOTES_REQUIRED = 5;

    @Autowired
    private QuestChallengeRepository challengeRepository;

    @Autowired
    private QuestParticipantRepository questParticipantRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private QuestSubmissionRepository submissionRepository;

    @Autowired
    private ContestParticipantRepository contestParticipantRepository;

    public void processAllSubmissions(final Contest contest) {
        List<QuestChallenge> challenges = challengeRepository.findByContest(contest);
        for (QuestChallenge challenge : challenges) {
            // find all submissions for challenge newer than last processed
            List<Notification> submissions = getAllSubmissions(challenge.getHashtag(), contest.getHashtag());
            for (Notification submission : submissions) {
                if (ignoreSubmission(challenge, submission)) {
                    // if the submission is not eligible for processing, skip
                    continue;
                }
                QuestParticipant questParticipant = getQuestParticipant(contest, submission);
                if (questParticipant == null) {
                    // if participant not found, skip
                    continue;
                }

                QuestSubmission questSubmission = submissionRepository.findByChallengeAndParticipant(challenge, questParticipant);

                if (questSubmission != null && questSubmission.getSubmissionState() == QuestSubmissionState.ACCEPTED) {
                    // skip, if does not exist or participant already solved the challenge
                    continue;
                }
                if (questSubmission == null) {
                    questSubmission = new QuestSubmission();
                }
                updateSubmission(questSubmission, challenge, questParticipant, submission);
            }
        }
    }

    public void processRecentSubmissions(final Contest contest) {
        Long sinceId = getLastProcessedId(contest);

        List<QuestChallenge> challenges = challengeRepository.findByContest(contest);
        for (QuestChallenge challenge : challenges) {
            // find all submissions for challenge newer than last processed
            List<Notification> submissions = getAllSubmissionsSinceId(challenge.getHashtag(), contest.getHashtag(), sinceId);
            for (Notification submission : submissions) {
                if (ignoreSubmission(challenge, submission)) {
                    // if the submission is not eligible for processing, skip
                    continue;
                }
                QuestParticipant questParticipant = getQuestParticipant(contest, submission);
                if (questParticipant == null) {
                    // if participant not found, skip
                    continue;
                }

                QuestSubmission questSubmission = submissionRepository.findByChallengeAndParticipant(challenge, questParticipant);

                if (questSubmission != null && questSubmission.getSubmissionState() == QuestSubmissionState.ACCEPTED) {
                    // skip, if does not exist or participant already solved the challenge
                    continue;
                }
                if (questSubmission == null) {
                    questSubmission = new QuestSubmission();
                }
                updateSubmission(questSubmission, challenge, questParticipant, submission);
            }
        }
    }

    private QuestSubmission updateSubmission(final QuestSubmission questSubmission, final QuestChallenge challenge, final QuestParticipant questParticipant, final Notification submission) {
        questSubmission.setChallenge(challenge);
        questSubmission.setParticipant(questParticipant);
        questSubmission.setSubmissionState(QuestSubmissionState.PENDING);
        questSubmission.setQuestPoints(challenge.getDefaultPoints());
        questSubmission.setText(submission.getBody());
        questSubmission.setCreated(submission.getTimestamp());
        questSubmission.setImageUrl(submission.getImageUrl());
        questSubmission.setVideoUrl(submission.getVideoUrl());
        questSubmission.setNotificationId(submission.getId());

        return submissionRepository.saveAndFlush(questSubmission);
    }

    private QuestParticipant getQuestParticipant(final Contest contest, final Notification submission) {
        QuestParticipant questParticipant = null;
        switch (submission.getNotificationType()) {
            case TWITTER:
                questParticipant = questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, submission.getAuthorUsername());
                break;
            case INSTAGRAM:
                questParticipant = questParticipantRepository.findByContestAndContestParticipantInstagramUsernameIgnoreCase(contest, submission.getAuthorUsername());
                break;
            case VINE:
                questParticipant = questParticipantRepository.findByContestAndContestParticipantVineUsernameIgnoreCase(contest, submission.getAuthorUsername());
                break;
        }
        if (questParticipant == null) {
            ContestParticipant contestParticipant = getContestParticipantByNotification(submission);
            if (contestParticipant == null) {
                return null;
            }
            questParticipant = new QuestParticipant();
            questParticipant.setContestParticipant(contestParticipant);
            questParticipant.setContest(submission.getContest());
            questParticipant = questParticipantRepository.save(questParticipant);
        }
        return questParticipant;

    }

    private ContestParticipant getContestParticipantByNotification(Notification notification) {
        String username = notification.getAuthorUsername();
        switch (notification.getNotificationType()) {
            case TWITTER:
                return contestParticipantRepository.findByTwitterUsernameIgnoreCase(username);
            case INSTAGRAM:
                return contestParticipantRepository.findByInstagramUsernameIgnoreCase(username);
            case VINE:
                return contestParticipantRepository.findByVineUsernameIgnoreCase(username);
        }
        return null;
    }

    private boolean ignoreSubmission(QuestChallenge challenge, Notification submission) {
        if (challenge.getEndDate() != null && submission.getTimestamp().after(challenge.getEndDate())) {
            // skip this submission, if it was posted after deadline
            return true;
        }
        if (submission.getParentId() != null) {
            // skip retweeted submission
            return true;
        }
        return false;
    }

    public List<Notification> getAllSubmissions(final String challengeHashtag, final String contestHashtag) {
        return notificationRepository.findByHashTags("%|" + challengeHashtag + "|%", "%|" + contestHashtag + "|%");
    }

    public List<Notification> getAllSubmissionsSinceId(final String challengeHashtag, final String contestHashtag, final long sinceId) {
        return notificationRepository.findByHashTagsSinceId("%|" + challengeHashtag + "|%", "%|" + contestHashtag + "|%", sinceId);
    }

    protected Long getLastProcessedId(final Contest contest) {
        Long max = submissionRepository.getMaxNotificationId(contest);
        return max != null ? max : 0L;
    }

    public void moveVotingToNextRound(final Contest contest) {
        // determite winner
        QuestSubmission winningSubmission = null;
        int maxVotes = -1;
        List<QuestSubmission> inProgressSubmissions = submissionRepository.getVoteInProgressSubmissions(contest);
        for (QuestSubmission submission : inProgressSubmissions) {
            int votes = submission.getVotes();
            if (votes >= MIN_VOTES_REQUIRED && votes > maxVotes) {
                // add point only if it has at least 5 votes
                maxVotes = votes;
                winningSubmission = submission;
            }
            submission.setVotes(0);
            submission.setVoteSubmissionState(null);
        }
        // process winner
        if (winningSubmission != null && inProgressSubmissions.size() == VOTE_ROUND_SIZE) {
            winningSubmission.setVoteSubmissionState(VoteSubmissionState.VOTE_WINNER);
            winningSubmission.getParticipant().setPointsFromVoting(winningSubmission.getParticipant().getPointsFromVoting());
            winningSubmission.setVotes(maxVotes);
            questParticipantRepository.save(winningSubmission.getParticipant());
        }
        submissionRepository.save(inProgressSubmissions);

        Date deadline = contest.getQuestConfiguration().getDeadline();
        if (deadline == null || deadline.after(new Date())) {
            // select new candidates
            Pageable pageable = new PageRequest(0, 4);
            List<QuestSubmission> newInProgressSubmissions = submissionRepository.getVoteEligableSubmissions(contest, pageable);
            for (QuestSubmission submission : newInProgressSubmissions) {
                submission.setVoteSubmissionState(VoteSubmissionState.IN_PROGRESS);
            }
            submissionRepository.save(newInProgressSubmissions);
        }
    }
}
