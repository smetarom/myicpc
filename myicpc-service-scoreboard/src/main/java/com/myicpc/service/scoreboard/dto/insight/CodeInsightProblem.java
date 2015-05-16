package com.myicpc.service.scoreboard.dto.insight;

import com.myicpc.model.codeInsight.CodeInsightActivity;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.service.scoreboard.insight.CodeInsightService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Roman Smetana
 */
public class CodeInsightProblem implements Serializable {
    private Map<Long, CodeInsightTeam> teamMap = new HashMap<>();
    private Problem problem;

    public CodeInsightProblem(Problem problem) {
        this.problem = problem;
    }

    public void addTeamActivity(CodeInsightActivity activity) {
        CodeInsightTeam team = teamMap.get(activity.getTeam().getId());
        if (team == null) {
            team = new CodeInsightTeam(activity);
        } else {
            team.merge(activity);
        }
        teamMap.put(team.getTeam().getId(), team);
    }

    public CodeInsightTeam getTeamById(Long id) {
        return teamMap.get(id);
    }

    public List<CodeInsightTeam> getTeamsSorted(final CodeInsightService.InsideCodeMode insideCodeMode) {
        if (insideCodeMode == CodeInsightService.InsideCodeMode.DIFF) {
            return getTeamsSortedByDiffLines();
        }
        if (insideCodeMode == CodeInsightService.InsideCodeMode.COUNT) {
            return getTeamsSortedByCountLines();
        }
        return null;
    }

    protected List<CodeInsightTeam> getTeamsSortedByDiffLines() {
        List<CodeInsightTeam> teams = new ArrayList<>(teamMap.values());
        Collections.sort(teams, new CodeInsightTeam.DiffLineCountComparator());
        return teams;
    }

    protected List<CodeInsightTeam> getTeamsSortedByCountLines() {
        List<CodeInsightTeam> teams = new ArrayList<>(teamMap.values());
        Collections.sort(teams, new CodeInsightTeam.LineCountComparator());
        return teams;
    }
}
