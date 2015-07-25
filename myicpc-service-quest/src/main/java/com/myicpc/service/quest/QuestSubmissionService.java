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
import com.myicpc.service.publish.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Service responsible for processing {@link QuestSubmission}
 * <p/>
 * Service searches {@link Notification}s for quest submissions and parse them to
 * {@link QuestSubmission}s
 * <p/>
 * Service processes the voting lifecycle of quest submissions
 *
 * @author Roman Smetana
 */
@Service
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

    @Autowired
    private PublishService publishService;

    /**
     * Searches through all {@link Notification}s and parse missing notifications into {@link QuestSubmission}s
     *
     * @param contest contest
     */
    @Transactional(readOnly = true)
    public void processAllSubmissions(final Contest contest) {
        List<QuestChallenge> challenges = challengeRepository.findByContest(contest);
        QuestService.applyHashtagPrefix(contest.getQuestConfiguration().getHashtagPrefix(), challenges);
        for (QuestChallenge challenge : challenges) {
            // find all submissions for challenge newer than last processed
            List<Notification> submissions = getAllSubmissions(challenge.getHashtag(), contest.getHashtag(), contest);
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

    /**
     * Searches through new {@link Notification}s, which were created after the last check,
     * and parse missing notifications into {@link QuestSubmission}s
     *
     * @param contest contest
     */
    @Transactional(readOnly = true)
    public void processRecentSubmissions(final Contest contest) {
        Long sinceId = getLastProcessedId(contest);

        List<QuestChallenge> challenges = challengeRepository.findByContest(contest);
        QuestService.applyHashtagPrefix(contest.getQuestConfiguration().getHashtagPrefix(), challenges);
        for (QuestChallenge challenge : challenges) {
            // find all submissions for challenge newer than last processed
            List<Notification> submissions = getAllSubmissionsSinceId(challenge.getHashtag(), contest.getHashtag(), sinceId, contest);
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
        questSubmission.setNotification(submission);

        QuestSubmission questSubmissionPersisted = submissionRepository.saveAndFlush(questSubmission);
        publishService.broadcastQuestSubmission(questSubmissionPersisted, challenge.getContest().getCode());
        return questSubmissionPersisted;
    }

    private QuestParticipant getQuestParticipant(final Contest contest, final Notification notification) {
        QuestParticipant questParticipant = null;
        switch (notification.getNotificationType()) {
            case TWITTER:
                questParticipant = questParticipantRepository.findByContestAndContestParticipantTwitterUsernameIgnoreCase(contest, notification.getAuthorUsername());
                break;
            case INSTAGRAM:
                questParticipant = questParticipantRepository.findByContestAndContestParticipantInstagramUsernameIgnoreCase(contest, notification.getAuthorUsername());
                break;
            case VINE:
                questParticipant = questParticipantRepository.findByContestAndContestParticipantVineUsernameIgnoreCase(contest, notification.getAuthorUsername());
                break;
        }
        if (questParticipant == null) {
            ContestParticipant contestParticipant = getContestParticipantByNotification(notification);
            if (contestParticipant == null) {
                return null;
            }
            questParticipant = new QuestParticipant();
            questParticipant.setContestParticipant(contestParticipant);
            questParticipant.setContest(notification.getContest());
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
        if (challenge.getEndDate() != null && submission.getTimestamp() != null && submission.getTimestamp().after(challenge.getEndDate())) {
            // skip this submission, if it was posted after deadline
            return true;
        }
        if (submission.getParentId() != null) {
            // skip retweeted submission
            return true;
        }
        return false;
    }

    private List<Notification> getAllSubmissions(final String challengeHashtag, final String contestHashtag, final Contest contest) {
        return notificationRepository.findByHashTagsAndContest("%|" + challengeHashtag + "|%", "%|" + contestHashtag + "|%", contest);
    }

    private List<Notification> getAllSubmissionsSinceId(final String challengeHashtag, final String contestHashtag, final long sinceId, final Contest contest) {
        return notificationRepository.findByHashTagsAndContestSinceId("%|" + challengeHashtag + "|%", "%|" + contestHashtag + "|%", sinceId, contest);
    }

    private Long getLastProcessedId(final Contest contest) {
        Long max = submissionRepository.getMaxNotificationId(contest);
        return max != null ? max : 0L;
    }

    /**
     * Controls the quest submission voting lifecycle
     * <p/>
     * It evaluates current voting round and finds a submission with most votes. This
     * submission is winner, if it has more votes than {@link QuestSubmissionService#MIN_VOTES_REQUIRED}
     * <p/>
     * If the quest deadline was not reached, it randomly finds next submissions to next voting round
     *
     * @param contest contest
     * @return winning submission
     */
    @Transactional
    public QuestSubmission moveVotingToNextRound(final Contest contest) {
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

        return winningSubmission;
    }
}
