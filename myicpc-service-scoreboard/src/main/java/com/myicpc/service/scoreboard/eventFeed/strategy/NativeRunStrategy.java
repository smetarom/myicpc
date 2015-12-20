package com.myicpc.service.scoreboard.eventFeed.strategy;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.LastTeamProblem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scoreboard strategy, which calculates the scoreboard state only from the data received from the Event feed
 *
 * @author Roman Smetana
 */
@Component
public class NativeRunStrategy extends FeedRunStrategy {
    /**
     * Calculates the current scoreboard state
     *
     * It uses {@link TeamProblem}s to calculate the scoreboard updates
     *
     * @param teamProblem solved team submission
     * @param contest     contest
     * @return list of changed teams
     */
    @Override
    protected List<Team> processScoreboardChanges(TeamProblem teamProblem, Contest contest) {
        markIfFirstProblemSolution(teamProblem);
        return computeNewRank(teamProblem.getTeam(), contest.getPenalty(), contest);
    }

    /**
     * Decides, if the run is the first problem, which solves a problem or not
     *
     * @param teamProblem received team submission
     */
    private void markIfFirstProblemSolution(final TeamProblem teamProblem) {
        List<TeamProblem> firstSubmissions = teamProblemRepository.findByProblemAndFirstSolved(teamProblem.getProblem(), true);
        if (firstSubmissions.isEmpty()) {
            // if there is no solved problem for a problem, mark this submission
            // as the first solution
            teamProblem.setFirstSolved(true);
        } else if (firstSubmissions.size() == 1) {
            TeamProblem currentFirstSolution = firstSubmissions.get(0);
            if (Double.compare(currentFirstSolution.getTime(), teamProblem.getTime()) == 1) {
                /*
                 * if there is the first solved problem for a problem, but
                 * submission time is after this submission, mark this submission as
                 * the first solution
                 *
                 * this can be caused by submission rejudgement
                 */
                removeFirstSolvedProblem(currentFirstSolution.getProblem());
                teamProblem.setFirstSolved(true);
            }
        }
    }

    /**
     * Computes a new rank for each team based on the change
     *
     * @param team team, which solved a problem
     * @param contest contest
     * @return teams, where the rank changed
     */
    private List<Team> computeNewRank(final Team team, final double penalty, Contest contest) {
        List<LastTeamProblem> list = lastTeamProblemRepository.findByTeam(team);
        int solved = 0;
        double totalTime = 0;
        for (LastTeamProblem lastTeamProblem : list) {
            if (lastTeamProblem.getTeamProblem() != null) {
                if (lastTeamProblem.getTeamProblem().getSolved()) {
                    solved++;
                    if (lastTeamProblem.getTeamProblem().getTime() != null && lastTeamProblem.getTeamProblem().getAttempts() != null) {
                        // add to total time the submission time and all
                        // penalties
                        totalTime += Math.floor(lastTeamProblem.getTeamProblem().getTime() / 60 + penalty
                                * (lastTeamProblem.getTeamProblem().getAttempts() - 1));
                    }
                }

            }
        }
        team.setProblemsSolved(solved);
        team.setTotalTime((int) Math.floor(totalTime));

        List<Team> teams = teamRepository.findByContest(contest);

        // sort teams
        Collections.sort(teams, new ScoreboardComparator(teamProblemRepository));
        int rank = 1;
        List<Team> teamsToBroadcast = new ArrayList<>();
        // reassign ranks based on sorted teams and mark team, where the rank
        // was changed
        for (Team t : teams) {
            if (t.getRank() != rank) {
                teamsToBroadcast.add(t);
            }
            t.setRank(rank);
            rank++;
        }
        teamRepository.save(teams);
        return teamsToBroadcast;
    }

    /**
     * Rank comparator based on ICPC rules
     *
     * @author Roman Smetana
     */
    public static class ScoreboardComparator implements Comparator<Team>, Serializable {
        private static final long serialVersionUID = -3436155626630004031L;
        private final Map<Long, Double> lastAcceptedCache = new HashMap<Long, Double>();
        private final TeamProblemRepository teamProblemRepository;

        public ScoreboardComparator(final TeamProblemRepository teamProblemRepository) {
            this.teamProblemRepository = teamProblemRepository;
        }

        /**
         * Teams are ranked according to the most problems solved. Teams placing
         * who solve the same number of problems are ranked first by least total
         * time and, if need be, by the earliest time of submission of the last
         * accepted run.
         *
         * @param team1
         * @param team2
         * @return 1 if team2 is higher in the rank than team1, 0 when they are
         * equal, and -1 if team1 is higher in the rank than team2
         */
        @Override
        public int compare(final Team team1, final Team team2) {
            if (!team1.getProblemsSolved().equals(team2.getProblemsSolved())) {
                if (team1.getProblemsSolved().equals(0) && !team2.getProblemsSolved().equals(0)) {
                    return 1;
                } else if (!team1.getProblemsSolved().equals(0) && team2.getProblemsSolved().equals(0)) {
                    return -1;
                } else {
                    return -1 * team1.getProblemsSolved().compareTo(team2.getProblemsSolved());
                }
            }
            if (!team1.getTotalTime().equals(team2.getTotalTime())) {
                return team1.getTotalTime().compareTo(team2.getTotalTime());
            }
            double lastTeam1 = getCachedValue(team1);
            double lastTeam2 = getCachedValue(team2);
            if (lastTeam1 == 0 && lastTeam2 != 0) {
                return -1;
            } else if (lastTeam1 != 0 && lastTeam2 == 0) {
                return 1;
            } else {
                return -1 * Double.compare(lastTeam1, lastTeam2);
            }
        }

        private double getCachedValue(Team team) {
            double lastTeam = 0;
            if (lastAcceptedCache.containsKey(team.getId())) {
                lastTeam = lastAcceptedCache.get(team.getId());
            } else {
                Object o = teamProblemRepository.getLastAcceptedTeamProblemTime(team);
                if (o != null) {
                    lastTeam = (Double) o;
                }
                lastAcceptedCache.put(team.getId(), lastTeam);
            }
            return lastTeam;
        }
    }
}
