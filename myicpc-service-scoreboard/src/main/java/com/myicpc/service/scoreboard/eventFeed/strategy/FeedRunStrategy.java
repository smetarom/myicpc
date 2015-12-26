package com.myicpc.service.scoreboard.eventFeed.strategy;

import com.myicpc.dto.eventFeed.parser.EventFeedSettingsDTO;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.LastTeamProblem;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.repository.eventFeed.LastTeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRankHistoryRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.listener.ScoreboardListener;
import com.myicpc.service.publish.PublishService;
import com.myicpc.service.scoreboard.ScoreboardService;
import com.myicpc.service.scoreboard.ScoreboardServiceImpl;
import com.myicpc.service.scoreboard.eventFeed.AnalystMessageService;
import com.myicpc.service.scoreboard.exception.EventFeedException;
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

/**
 * Common service for all scoreboard calculation strategies
 * <p/>
 * It defines the basic logic for incoming {@link TeamProblem}s
 *
 * @author Roman Smetana
 */
public abstract class FeedRunStrategy {
    private static final Logger logger = LoggerFactory.getLogger(FeedRunStrategy.class);
    private static final String SUBMISSION_FRESH = "fresh";
    private static final String SUBMISSION_DONE = "done";

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

    @Autowired(required = false)
    private AnalystMessageService analystMessageService;

    @Autowired(required = false)
    protected ScoreboardService scoreboardService;

    @Autowired(required = false)
    protected ProblemService problemService;

    @PostConstruct
    private void init() {
        if (analystMessageService != null) {
            scoreboardListeners.add(analystMessageService);
        }
        if (scoreboardService != null) {
            scoreboardListeners.add(scoreboardService);
        }
        if (problemService != null) {
            scoreboardListeners.add(problemService);
        }
    }

    /**
     * Processes {@link TeamProblem}
     * <p/>
     * It persists {@code teamProblem}, updates the scoreboard according to
     * new {@code teamProblem} and publishes the changes
     *
     * @param teamProblem incoming team submission
     * @param contest     contest
     * @param eventFeedSettings additional event feed parsing settings
     * @return processed team submission
     * @throws EventFeedException error on updating the scoreboard changes
     */
    public TeamProblem executeTeamProblem(TeamProblem teamProblem, Contest contest, EventFeedSettingsDTO eventFeedSettings) throws EventFeedException {
        List<Team> teamsToBroadcast = new ArrayList<>();
        // fresh = submitted run, done = judged run
        if (SUBMISSION_FRESH.equalsIgnoreCase(teamProblem.getStatus())) {
            teamProblem = teamProblemRepository.save(teamProblem);
            changeLastSubmissionByTeam(teamProblem);
            if (eventFeedSettings.isSkipMessageGeneration()) {
                sendAutogeneratedMessages(teamProblem, contest, NotificationType.SCOREBOARD_SUBMIT);
            }
        } else if (SUBMISSION_DONE.equalsIgnoreCase(teamProblem.getStatus())) {
            // if there is no pending submission for the same run, persist it
            teamProblem = teamProblemRepository.save(teamProblem);

            changeLastSubmissionByTeam(teamProblem);
            teamProblem.setAttempts(countTeamProblemAttempts(teamProblem));
            teamProblem.setOldRank(teamProblem.getTeam().getRank());
            if (teamProblem.getSolved()) {
                teamsToBroadcast.addAll(processScoreboardChanges(teamProblem, contest));
            }
            teamProblem.setNewRank(teamProblem.getTeam().getRank());
            teamProblem = teamProblemRepository.save(teamProblem);
            if (teamProblem.getSolved() && eventFeedSettings.isSkipMessageGeneration()) {
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
        LastTeamProblem lastTeamProblem = lastTeamProblemRepository.findByTeamAndProblemWithTeamSubmission(teamProblem.getTeam(), teamProblem.getProblem());
        if (lastTeamProblem == null) {
            lastTeamProblem = new LastTeamProblem();
            lastTeamProblem.setTeam(teamProblem.getTeam());
            lastTeamProblem.setProblem(teamProblem.getProblem());
            lastTeamProblem.setContest(teamProblem.getTeam().getContest());
        }
        // the problem was already solved or we have already a newer submission
        if (lastTeamProblem.getTeamProblem() != null
                && (lastTeamProblem.getTeamProblem().getSolved() || lastTeamProblem.getTeamProblem().getTime().compareTo(teamProblem.getTime()) > 0)) {
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
     * Remove the mark on the team submission about the first solved problem for given problem
     *
     * @param problem problem
     */
    protected void removeFirstSolvedProblem(final Problem problem) {
        List<TeamProblem> firstSolvedTeamProblems = teamProblemRepository.findByProblemAndFirstSolved(problem, true);
        for (TeamProblem fstp : firstSolvedTeamProblems) {
            fstp.setFirstSolved(false);
            teamProblemRepository.save(fstp);
            publishService.broadcastTeamProblem(ScoreboardServiceImpl.getTeamSubmissionJSON(fstp, Collections.EMPTY_LIST),
                    problem.getContest().getCode());
        }
    }

    /**
     * Process the successful run
     * <p/>
     * The scoreboard must be updated, because with a solved {@code teamProblem}
     * the scoreboard might change. How the current state of the scoreboard is retrieved
     * depends on the chosen strategy
     *
     * @param teamProblem solved team submission
     * @param contest     contest
     * @return list of affected teams
     */
    protected abstract List<Team> processScoreboardChanges(final TeamProblem teamProblem, Contest contest) throws EventFeedException;

    private void sendAutogeneratedMessages(TeamProblem teamProblem, Contest contest, NotificationType notificationType) {
        if (contest.getContestSettings() != null && contest.getContestSettings().isGenerateMessages()) {
            try {
                if (notificationType.isScoreboardSubmitted()) {
                    onPendingSubmissionNotification(teamProblem);
                } else if (notificationType.isScoreboardSuccess()) {
                    onSuccessSubmissionNotification(teamProblem);
                } else if (notificationType.isScoreboardFailed()) {
                    onFailedSubmissionNotification(teamProblem);
                }
            } catch (Throwable throwable) {
                logger.error("Message for run with ID " + teamProblem.getId() + " was not generated and broadcasted.", throwable);
            }
        }
    }

    private void onPendingSubmissionNotification(final TeamProblem teamProblem) {
        for (ScoreboardListener listener : scoreboardListeners) {
            listener.onPendingSubmissionNotification(teamProblem);
        }
    }

    private void onFailedSubmissionNotification(final TeamProblem teamProblem) {
        for (ScoreboardListener listener : scoreboardListeners) {
            listener.onFailedSubmissionNotification(teamProblem);
        }
    }

    private void onSuccessSubmissionNotification(final TeamProblem teamProblem) {
        for (ScoreboardListener listener : scoreboardListeners) {
            listener.onSuccessSubmissionNotification(teamProblem);
        }
    }

    private void onSubmission(final TeamProblem teamProblem, final List<Team> effectedTeams) {
        for (ScoreboardListener listener : scoreboardListeners) {
            listener.onSubmission(teamProblem, effectedTeams);
        }
    }
}
