package com.myicpc.service.scoreboard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.LastTeamProblem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.listener.ScoreboardListenerAdapter;
import com.myicpc.service.publish.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Roman Smetana
 */
@Service("scoreboardService")
@Transactional
public class ScoreboardServiceImpl extends ScoreboardListenerAdapter implements ScoreboardService {

    @Autowired
    private PublishService publishService;

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public void onSubmission(TeamProblem teamProblem, List<Team> effectedTeams) {
        JsonObject tpObject = new JsonObject();
        tpObject.addProperty("type", "submission");
        tpObject.addProperty("teamId", teamProblem.getTeam().getId());
        tpObject.addProperty("teamName", teamProblem.getTeam().getName());
        tpObject.addProperty("problemId", teamProblem.getProblem().getId());
        tpObject.addProperty("problemCode", teamProblem.getProblem().getCode());
        tpObject.addProperty("problemName", teamProblem.getProblem().getName());
        tpObject.addProperty("attempts", teamProblem.getAttempts());
        tpObject.addProperty("judged", teamProblem.getJudged());
        tpObject.addProperty("solved", teamProblem.getSolved());
        tpObject.addProperty("passed", teamProblem.getNumTestPassed());
        tpObject.addProperty("first", teamProblem.isFirstSolved());
        tpObject.addProperty("testcases", teamProblem.getProblem().getTotalTestcases());
        tpObject.addProperty("penalty", teamProblem.getPenalty());
        tpObject.addProperty("time", teamProblem.getTime());
        tpObject.addProperty("total", teamProblem.getTeam().getTotalTime());
        tpObject.addProperty("numSolved", teamProblem.getTeam().getProblemsSolved());
        tpObject.addProperty("oldRank", teamProblem.getOldRank());
        tpObject.addProperty("teamRank", teamProblem.getTeam().getRank());

        JsonObject teamsObject = new JsonObject();
        for (Team team : effectedTeams) {
            JsonObject teamObject = new JsonObject();
            teamObject.addProperty("teamId", team.getId());
            teamObject.addProperty("teamRank", team.getRank());
            teamsObject.add(team.getId().toString(), teamObject);
        }
        tpObject.add("teams", teamsObject);

        publishService.broadcastTeamProblem(tpObject, teamProblem.getTeam().getContest().getCode());
    }

    /**
     * Returns JSON representation of all teams paired with last team
     * submissions
     *
     * @return JSON representation of all teams paired with last team
     *         submissions
     */
    @Override
    public JsonArray getTeamsFullTemplate(final Contest contest) {
        Iterable<Team> teams = teamRepository.findByContest(contest);

        return getTeamsFullTemplate(teams);
    }

    /**
     * Returns JSON representation of selected teams paired with last team
     * submissions
     *
     * @param teamIds
     *            ids of selected teams
     * @return JSON representation of selected teams paired with last team
     *         submissions
     */
    @Override
    public JsonArray getTeamsFullTemplate(final Contest contest, final List<Long> teamIds) {
        Iterable<Team> teams = teamRepository.findByContestAndTeamId(contest, teamIds);

        return getTeamsFullTemplate(teams);
    }

    /**
     * Returns JSON representation of selected teams paired with last team
     * submissions
     *
     * @param teams
     *            selected teams
     * @return JSON representation of selected teams paired with last team
     *         submissions
     */
    public JsonArray getTeamsFullTemplate(final Iterable<Team> teams) {
        JsonArray root = new JsonArray();
        for (Team team : teams) {
            final Contest contest = team.getContest();
            JsonObject teamObject = new JsonObject();
            teamObject.addProperty("teamId", team.getId());
            teamObject.addProperty("teamExternalId", team.getExternalId());
            teamObject.addProperty("teamRank", team.getRank());
            teamObject.addProperty("teamName", team.getShortName());
            if (team.getRegion() != null && contest.getContestSettings().isShowRegion()) {
                teamObject.addProperty("regionName", team.getRegion().getShortName());
                teamObject.addProperty("regionId", team.getRegion().getId());
            }
            if (contest.getContestSettings().isShowUniversity()) {
                teamObject.addProperty("universityName", team.getUniversityName());
            }
            if (contest.getContestSettings().isShowCountry()) {
                teamObject.addProperty("nationality", team.getNationality());
            }
            teamObject.addProperty("nSolved", team.getProblemsSolved());
            teamObject.addProperty("totalTime", team.getTotalTime());
            teamObject.addProperty("followed", team.isFollowed());

            JsonObject teamProblems = new JsonObject();
            List<LastTeamProblem> problems = team.getLastTeamProblems();

            for (LastTeamProblem lastTeamProblem : problems) {
                JsonObject problemObject = new JsonObject();
                TeamProblem teamProblem = lastTeamProblem.getTeamProblem();
                problemObject.addProperty("attempts", teamProblem.getAttempts());
                problemObject.addProperty("judged", teamProblem.getJudged());
                problemObject.addProperty("solved", teamProblem.getSolved());
                problemObject.addProperty("penalty", teamProblem.getPenalty());
                problemObject.addProperty("time", teamProblem.getTime());
                problemObject.addProperty("first", teamProblem.isFirstSolved());
                teamProblems.add(lastTeamProblem.getProblem().getId().toString(), problemObject);
            }
            teamObject.add("teamProblems", teamProblems);

            root.add(teamObject);
        }
        return root;
    }
}
