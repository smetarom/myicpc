package com.myicpc.service.scoreboard;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.LastTeamProblem;
import com.myicpc.dto.eventFeed.LastTeamSubmissionDTO;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.model.teamInfo.University;
import com.myicpc.repository.eventFeed.LastTeamProblemRepository;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.listener.ScoreboardListenerAdapter;
import com.myicpc.service.publish.PublishService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Service("scoreboardService")
@Transactional
public class ScoreboardServiceImpl extends ScoreboardListenerAdapter implements ScoreboardService {

    @PersistenceContext(name = "MyICPC")
    protected EntityManager em;

    @Autowired
    private PublishService publishService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamInfoRepository teamInfoRepository;

    @Autowired
    private LastTeamProblemRepository lastTeamProblemRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public void onSubmission(TeamProblem teamProblem, List<Team> effectedTeams) {
        JsonObject tpObject = new JsonObject();
        tpObject.addProperty("type", "submission");
        tpObject.addProperty("teamId", teamProblem.getTeam().getExternalId());
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
            teamObject.addProperty("teamId", team.getExternalId());
            teamObject.addProperty("teamRank", team.getRank());
            teamsObject.add(team.getExternalId().toString(), teamObject);
        }
        tpObject.add("teams", teamsObject);

        publishService.broadcastTeamProblem(tpObject, teamProblem.getTeam().getContest().getCode());
    }

    /**
     * Returns JSON representation of all teams paired with last team
     * submissions
     *
     * @return JSON representation of all teams paired with last team
     * submissions
     */
    @Override
    public JsonArray getTeamsFullTemplate(final Contest contest) {
        List<Team> teams = teamRepository.findByContest(contest);
        List<LastTeamSubmissionDTO> submissions = lastTeamProblemRepository.findLastTeamSubmissionDTOByContest(contest);
        return getTeamsFullTemplate(teams, submissions);

    }

    /**
     * Returns JSON representation of selected teams paired with last team
     * submissions
     *
     * @param teamIds ids of selected teams
     * @return JSON representation of selected teams paired with last team
     * submissions
     */
    @Override
    public JsonArray getTeamsFullTemplate(final Contest contest, final List<Long> teamIds) {
        // TODO renew or remove
//        Iterable<Team> teams = teamRepository.findByContestAndTeamIds(contest, teamIds);
//
//        return getTeamsFullTemplate(teams);
        return null;
    }

    /**
     * Returns JSON representation of selected teams paired with last team
     * submissions
     *
     * @param teams selected teams
     * @return JSON representation of selected teams paired with last team
     * submissions
     */
    public JsonArray getTeamsFullTemplate(final List<Team> teams, final List<LastTeamSubmissionDTO> submissions) {
        Multimap<Long, LastTeamSubmissionDTO> submissionMultimap = HashMultimap.create();
        for (LastTeamSubmissionDTO submission : submissions) {
            submissionMultimap.put(submission.getTeamId(), submission);
        }

        JsonArray root = new JsonArray();
        for (Team team : teams) {
            final Contest contest = team.getContest();
            JsonObject teamObject = new JsonObject();
            teamObject.addProperty("teamId", team.getExternalId());
            teamObject.addProperty("teamExternalId", team.getExternalId());
            teamObject.addProperty("teamRank", team.getRank());
            teamObject.addProperty("teamName", team.getName());
            if (contest.getContestSettings().isShowCountry()) {
                teamObject.addProperty("nationality", team.getNationality());
            }
            teamObject.addProperty("nSolved", team.getProblemsSolved());
            teamObject.addProperty("totalTime", team.getTotalTime());
            teamObject.addProperty("followed", team.isFollowed());

            JsonObject teamProblems = new JsonObject();
            for (LastTeamSubmissionDTO submission : submissionMultimap.get(team.getId())) {
                JsonObject problemObject = new JsonObject();
                problemObject.addProperty("attempts", submission.getAttempts());
                problemObject.addProperty("judged", submission.isJudged());
                problemObject.addProperty("solved", submission.isSolved());
                problemObject.addProperty("penalty", submission.isPenalty());
                problemObject.addProperty("time", submission.getTime());
                problemObject.addProperty("first", submission.isFirstSolved());
                teamProblems.add(String.valueOf(submission.getProblemId()), problemObject);
            }
            teamObject.add("teamProblems", teamProblems);

            root.add(teamObject);
        }
        return root;
    }

    @Override
    public JsonArray getTeamsScorebarTemplate(final Contest contest) {
        JsonArray arr = new JsonArray();
        List<Team> teams = teamRepository.findByContest(contest);

        for (Team team : teams) {
            List<LastTeamProblem> lastTeamProblems = lastTeamProblemRepository.findByTeam(team);
            JsonArray solved = new JsonArray();
            JsonArray failed = new JsonArray();

            for (LastTeamProblem lastTeamProblem : lastTeamProblems) {
                if (lastTeamProblem.getTeamProblem().getJudged()) {
                    JsonObject problem = new JsonObject();
                    problem.addProperty("code", lastTeamProblem.getProblem().getCode());
                    if (lastTeamProblem.getTeamProblem().getSolved()) {
                        solved.add(problem);
                    } else {
                        failed.add(problem);
                    }
                }
            }

            JsonObject teamObject = new JsonObject();
            String shortName = team.getTeamInfo() != null ? team.getTeamInfo().getShortName() : team.getName();
            String abbreviation = team.getTeamInfo() != null ? team.getTeamInfo().getAbbreviation() : team.getName();

            teamObject.addProperty("rank", team.getRank());
            teamObject.addProperty("teamId", team.getExternalId());
            teamObject.addProperty("teamShortName", StringEscapeUtils.escapeEcmaScript(shortName));
            teamObject.addProperty("teamAbbreviation", StringEscapeUtils.escapeEcmaScript(abbreviation));
            teamObject.addProperty("solvedNum", solved.size());
            teamObject.add("solved", solved);
            teamObject.addProperty("failedNum", failed.size());
            teamObject.add("failed", failed);

            arr.add(teamObject);
        }

        return arr;
    }

    @Override
    public JsonObject getTeamMapCoordinates(Contest contest) {
        List<TeamInfo> teamInfos = teamInfoRepository.findByContest(contest);
        JsonObject coordinates = new JsonObject();
        for (TeamInfo teamInfo : teamInfos) {
            University university = teamInfo.getUniversity();
            if (university != null) {
                JsonObject teamCoordinates = new JsonObject();
                teamCoordinates.addProperty("latitude", university.getLatitude());
                teamCoordinates.addProperty("longtitude", university.getLongtitude());
                teamCoordinates.addProperty("country", university.getCountry());
                coordinates.add(teamInfo.getExternalId().toString(), teamCoordinates);
            }
        }
        return coordinates;
    }

    @Override
    public JsonArray getProblemsJSON(Contest contest) {
        List<Problem> problems = problemRepository.findByContestOrderByCodeAsc(contest);
        JsonArray root = new JsonArray();
        for (Problem problem : problems) {
            JsonObject problemObject = new JsonObject();
            problemObject.addProperty("id", problem.getId());
            problemObject.addProperty("code", problem.getCode());
            problemObject.addProperty("name", problem.getName());
            root.add(problemObject);
        }
        return root;
    }
}
