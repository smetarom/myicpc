package com.myicpc.service.scoreboard.problem;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myicpc.dto.eventFeed.TeamSubmissionDTO;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.service.listener.ScoreboardListenerAdapter;
import com.myicpc.service.publish.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private TeamProblemRepository teamProblemRepository;

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
                teamProblem.getTotalNumTests());
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

    public static JsonObject getTeamSubmissionJSON(final TeamSubmissionDTO teamProblem) {
        // TODO remove
        Random random = new Random();
        JsonObject submissionJSON = new JsonObject();
        submissionJSON.addProperty("id", teamProblem.getTeamSubmissionId());
        submissionJSON.addProperty("time", teamProblem.getTime());
        submissionJSON.addProperty("judged", teamProblem.isJudged());
        submissionJSON.addProperty("solved", teamProblem.isSolved());
        submissionJSON.addProperty("passed", teamProblem.getNumTestPassed());
        submissionJSON.addProperty("testcases", teamProblem.getTotalNumTests());
        submissionJSON.addProperty("passed", random.nextInt(20));
        submissionJSON.addProperty("testcases", random.nextInt(50));
        submissionJSON.addProperty("teamExternalId", teamProblem.getTeamId());
        submissionJSON.addProperty("teamName", teamProblem.getTeamName());
        return submissionJSON;
    }
}
