package com.myicpc.master.service.scoreboard.strategy;

import com.myicpc.master.dao.EventFeedDao;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.LastTeamProblem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;

import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class NativeRunStrategy extends FeedRunStrategy {
    @Override
    protected List<Team> processScoreboardChanges(TeamProblem teamProblemFromDB, TeamProblem teamProblemFromFeed, Contest contest) {
        // TODO Mark if the first solved problem

        Team team = teamProblemFromDB.getTeam();
        List<Team> changedTeams = computeNewRank(team, contest.getPenalty());
        return changedTeams;
    }

    /**
     * Computes a new rank for each team based on the change
     *
     * @param team team, which solved a problem
     * @return teams, where the rank changed
     */
    private List<Team> computeNewRank(final Team team, final double penalty) {
        List<LastTeamProblem> list = eventFeedDao.findLastTeamProblemByTeam(team);
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
        eventFeedDao.save(team);

        List<Team> teams = eventFeedDao.findTeamByContest(team.getContest());

        // sort teams
        Collections.sort(teams, new ScorebaordComparator(eventFeedDao));
        int rank = 1;
        int oldRank = 0;
        List<Team> teamsToBroadcast = new ArrayList<Team>();
        // reassign ranks based on sorted teams and mark team, where the rank
        // was changed
        for (Team t : teams) {
            oldRank = t.getRank();
            t.setRank(rank);
            if (oldRank != rank) {
                teamsToBroadcast.add(t);
                // TODO renew the team history
//                TeamRankHistory history = new TeamRankHistory(t, rank, oldRank);
//                teamRankHistoryRepository.save(history);
            }
            rank++;
        }

        for (Team t : teams) {
            eventFeedDao.save(t);
        }
        eventFeedDao.flush();
        return teamsToBroadcast;
    }

    /**
     * Rank comparator based on ICPC rules
     *
     * @author Roman Smetana
     * @see <a
     * href="http://icpc.baylor.edu/worldfinals/rules#HScoringoftheFinals">http://icpc.baylor.edu/worldfinals/rules#HScoringoftheFinals</a>
     */
    public static class ScorebaordComparator implements Comparator<Team>, Serializable {
        private static final long serialVersionUID = -3436155626630004031L;
        private Map<Long, Double> lastAcceptedCache = new HashMap<Long, Double>();
        private EventFeedDao eventFeedDao;

        public ScorebaordComparator(final EventFeedDao eventFeedDao) {
            this.eventFeedDao = eventFeedDao;
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
                Object o = eventFeedDao.getLastAcceptedTeamProblemTime(team);
                if (o != null) {
                    lastTeam = (Double) o;
                }
                lastAcceptedCache.put(team.getId(), lastTeam);
            }
            return lastTeam;
        }
    }
}
