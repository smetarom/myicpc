package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.LastTeamProblem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.repository.eventFeed.LastTeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRankHistoryRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.listener.ScoreboardListener;
import com.myicpc.service.publish.PublishService;
import com.myicpc.service.scoreboard.ScoreboardService;
import com.myicpc.service.scoreboard.problem.ProblemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class FeedRunStrategy {
    private static final Logger logger = LoggerFactory.getLogger(FeedRunStrategy.class);

    private final Set<ScoreboardListener> scoreboardListeners = Collections.synchronizedSet(new HashSet<ScoreboardListener>());

    @Autowired
    private PublishService publishService;

    @Autowired
    protected TeamProblemRepository teamProblemRepository;

    @Autowired
    protected LastTeamProblemRepository lastTeamProblemRepository;

    @Autowired
    protected TeamRepository teamRepository;

    @Autowired
    protected TeamRankHistoryRepository teamRankHistoryRepository;
//
//    @Autowired(required = false)
//    protected ScoreboardListener notificationService;

    @Autowired(required = false)
    protected ScoreboardService scoreboardService;

    @Autowired(required = false)
    protected ProblemService problemService;

    @PostConstruct
    private void init() {
//        if (notificationService != null) {
//            scoreboardListeners.add(notificationService);
//        }
        if (scoreboardService != null) {
            scoreboardListeners.add(scoreboardService);
        }
        if (problemService != null) {
            scoreboardListeners.add(problemService);
        }
    }

    public TeamProblem executeTeamProblem(TeamProblem teamProblem, Contest contest) {
        List<Team> teamsToBroadcast = new ArrayList<>();
        // fresh = submitted run, done = judged run
        if ("fresh".equalsIgnoreCase(teamProblem.getStatus())) {
            teamProblem = teamProblemRepository.saveAndFlush(teamProblem);
            changeLastSubmissionByTeam(teamProblem);
            sendAutogeneratedMessages(teamProblem, contest, NotificationType.SCOREBOARD_SUBMIT);
        } else if ("done".equalsIgnoreCase(teamProblem.getStatus())) {
            // get submission, which is persist as pending
//            originalTeamProblem = teamProblemRepository.findBySystemIdAndTeamContest(teamProblem.getSystemId(), contest);
            // if there is no pending submission for the same run, persist it

            teamProblem = teamProblemRepository.save(teamProblem);
            // otherwise update it
            changeLastSubmissionByTeam(teamProblem);
            teamProblem.setAttempts(countTeamProblemAttempts(teamProblem));
//            originalTeamProblem.setOldRank(originalTeamProblem.getTeam().getRank());
            if (teamProblem.getSolved()) {
                teamsToBroadcast.addAll(processScoreboardChanges(teamProblem, contest));
            }
//            originalTeamProblem.setNewRank(originalTeamProblem.getTeam().getRank());
            teamProblem = teamProblemRepository.saveAndFlush(teamProblem);
            if (teamProblem.getSolved()) {
                sendAutogeneratedMessages(teamProblem, contest, NotificationType.SCOREBOARD_SUCCESS);
            }
        }
        onSubmission(teamProblem, teamsToBroadcast);
        return teamProblem;
    }

    /**
     * Update {@link LastTeamProblem} by {@link TeamProblem}
     *
     * @param teamProblem the latest team problem
     */
    protected void changeLastSubmissionByTeam(final TeamProblem teamProblem) {
        // TODO ugly generated SQL
        LastTeamProblem lastTeamProblem = lastTeamProblemRepository.findByTeamAndProblem(teamProblem.getTeam(), teamProblem.getProblem());
        if (lastTeamProblem == null) {
            lastTeamProblem = new LastTeamProblem();
            lastTeamProblem.setTeam(teamProblem.getTeam());
            lastTeamProblem.setProblem(teamProblem.getProblem());
            lastTeamProblem.setContest(teamProblem.getTeam().getContest());
        }
        // the problem was already solved or we have already a newer submission
        if (lastTeamProblem.getTeamProblem() != null
                && (lastTeamProblem.getTeamProblem().getSolved() || lastTeamProblem.getTeamProblem().getTime().compareTo(teamProblem.getTime()) == 1)) {
            return;
        }
        lastTeamProblem.setTeamProblem(teamProblem);
        lastTeamProblemRepository.save(lastTeamProblem);
    }

    /**
     * Counts a number of tries team had for this run
     *
     * @param tp run
     * @return number of tries
     */
    protected int countTeamProblemAttempts(final TeamProblem tp) {
        Iterable<TeamProblem> list = teamProblemRepository.findByTeamAndProblemOrderByTimeAsc(tp.getTeam(), tp.getProblem());
        int count = 0;
        for (TeamProblem teamProblem : list) {
            if (teamProblem.getPenalty() && !teamProblem.getSolved()) {
                count++;
            }
            if (teamProblem.getSolved()) {
                count++;
                break;
            }
        }
        return count;
    }

    /**
     * Process the successful run
     *
     * @param teamProblem   team loaded from database
     * @param contest
     * @return list of affected teams
     */
    protected abstract List<Team> processScoreboardChanges(final TeamProblem teamProblem, Contest contest);

    protected void sendAutogeneratedMessages(TeamProblem teamProblem, Contest contest, NotificationType notificationType) {
        if (contest.getContestSettings() != null && contest.getContestSettings().isGenerateMessages()) {
            try {
                if (notificationType.isScoreboardSubmitted()) {
                    onPendingSubmissionNotification(teamProblem);
//                    notificationService.sendNotificationOnTeamProblemSubmitted(teamProblem);
                } else if (notificationType.isScoreboardSuccess()) {
                    onSuccessSubmissionNotification(teamProblem);
//                    notificationService.sendNotificationOnTeamProblemSolved(teamProblem);
                }
            } catch (Throwable throwable) {
                logger.error("Message for run with ID " + teamProblem.getId() + " was not generated and broadcasted.", throwable);
            }
        }
    }

    protected void onPendingSubmissionNotification(final TeamProblem teamProblem) {
        for (ScoreboardListener listener : scoreboardListeners) {
            listener.onPendingSubmissionNotification(teamProblem);
        }
    }

    protected void onSuccessSubmissionNotification(final TeamProblem teamProblem) {
        for (ScoreboardListener listener : scoreboardListeners) {
            listener.onSuccessSubmissionNotification(teamProblem);
        }
    }
    protected void onSubmission(final TeamProblem teamProblem, final List<Team> effectedTeams) {
        for (ScoreboardListener listener : scoreboardListeners) {
            listener.onSubmission(teamProblem, effectedTeams);
        }
    }
}
