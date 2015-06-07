package com.myicpc.service.scoreboard.problem;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.myicpc.dto.eventFeed.TeamSubmissionDTO;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Judgement;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.repository.eventFeed.JudgementRepository;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.listener.ScoreboardListenerAdapter;
import com.myicpc.service.publish.PublishService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class ProblemServiceImpl extends ScoreboardListenerAdapter implements ProblemService {
    @Autowired
    private PublishService publishService;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamRepository teamRepository;


    @Autowired
    private TeamProblemRepository teamProblemRepository;

    @Autowired
    private JudgementRepository judgementRepository;

    @Override
    public void onSubmission(TeamProblem teamProblem, List<Team> effectedTeams) {
        TeamSubmissionDTO teamSubmissionDTO = new TeamSubmissionDTO(teamProblem.getSystemId(),
                teamProblem.getTeam().getExternalId(),
                teamProblem.getTeam().getName(),
                teamProblem.getSolved(),
                teamProblem.getPenalty(),
                teamProblem.getTime(),
                teamProblem.getJudged(),
                teamProblem.getNumTestPassed(),
                teamProblem.getTotalNumTests(),
                teamProblem.getLanguage(),
                teamProblem.getResultCode());
        publishService.broadcastProblem(ProblemServiceImpl.getTeamSubmissionJSON(teamSubmissionDTO),
                teamProblem.getProblem().getCode(),
                teamProblem.getTeam().getContest().getCode());
    }

    @Override
    public List<Problem> findByContest(Contest contest) {
        return problemRepository.findByContestOrderByCodeAsc(contest);
    }

    @Override
    public JsonArray getSubmissionAttemptsJSON(Problem problem) {
        List<TeamSubmissionDTO> submissions = teamProblemRepository.findTeamSubmissionsByProblem(problem);
        JsonArray arr = new JsonArray();

        for (TeamSubmissionDTO teamProblem : submissions) {
            arr.add(getTeamSubmissionJSON(teamProblem));
        }

        return arr;
    }

    @Override
    public JsonObject getAllJudgementsJSON(Contest contest) {
        List<Judgement> judgements = judgementRepository.findByContest(contest);
        JsonObject object = new JsonObject();
        for (Judgement judgement : judgements) {
            JsonObject judgementObject = new JsonObject();
            judgementObject.addProperty("code", judgement.getCode());
            judgementObject.addProperty("name", judgement.getName());
            judgementObject.addProperty("color", judgement.getColor());
            object.add(judgement.getCode(), judgementObject);
        }
        return object;
    }

    @Override
    public JsonArray getProblemOverviewJSON(Problem problem) {
        List<Team> teams = teamRepository.findByContestOrderByNameAsc(problem.getContest());
        List<TeamSubmissionDTO> submissions = teamProblemRepository.findTeamSubmissionsByProblem(problem);
        Map<Long, TeamSubmissionDTO> map = new HashMap<>(submissions.size());

        for (TeamSubmissionDTO submission : submissions) {
            if (submission.isJudged() && !map.containsKey(submission.getTeamId())) {
                map.put(submission.getTeamId(), submission);
            }
        }

        JsonArray arr = new JsonArray();
        for (Team team : teams) {
            JsonArray dataArr = new JsonArray();
            JsonObject teamObject = new JsonObject();
            teamObject.addProperty("teamExternalId", team.getExternalId());
            teamObject.addProperty("teamName", team.getName());
            dataArr.add(teamObject);
            TeamSubmissionDTO submission = map.get(team.getExternalId());
            if (submission != null) {
                dataArr.add(getTeamSubmissionJSON(submission));
            } else {
                JsonObject zeroPassed = new JsonObject();
                zeroPassed.addProperty("passed", 0);
                dataArr.add(zeroPassed);
            }
            arr.add(dataArr);
        }

        return arr;
    }

    public static JsonObject getTeamSubmissionJSON(final TeamSubmissionDTO teamProblem) {
        // TODO remove
        Random random = new Random();
        JsonObject submissionJSON = new JsonObject();
        submissionJSON.addProperty("id", teamProblem.getTeamSubmissionId());
        submissionJSON.addProperty("time", teamProblem.getTime());
        submissionJSON.addProperty("judged", teamProblem.isJudged());
        submissionJSON.addProperty("solved", teamProblem.isSolved());
        submissionJSON.addProperty("language", teamProblem.getLanguage());
        submissionJSON.addProperty("judgement", teamProblem.getJudgement());
        submissionJSON.addProperty("passed", teamProblem.getNumTestPassed());
        submissionJSON.addProperty("testcases", teamProblem.getTotalNumTests());
        submissionJSON.addProperty("passed", random.nextInt(50));
        submissionJSON.addProperty("testcases", 50);
        submissionJSON.addProperty("teamExternalId", teamProblem.getTeamId());
        submissionJSON.addProperty("teamName", teamProblem.getTeamName());
        return submissionJSON;
    }
}
