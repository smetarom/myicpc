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
 * Holds data for a {@link Problem} in Insight view
 *
 * It holds all {@link CodeInsightTeam}s which belong to this problem
 *
 * @author Roman Smetana
 */
public class CodeInsightProblem implements Serializable {
    private static final long serialVersionUID = -458122017645361481L;
    /**
     * Map between team ID and {@link CodeInsightTeam}
     */
    private final Map<Long, CodeInsightTeam> teamMap = new HashMap<>();

    /**
     * Adds a {@code activity} to the team
     *
     * @param activity code insight activity
     */
    public void addTeamActivity(CodeInsightActivity activity) {
        CodeInsightTeam team = teamMap.get(activity.getTeam().getId());
        if (team == null) {
            team = new CodeInsightTeam(activity);
        } else {
            team.merge(activity);
        }
        teamMap.put(team.getTeam().getId(), team);
    }

    /**
     * Return a {@link CodeInsightTeam} by team ID
     *
     * @param id team ID
     * @return {@link CodeInsightTeam} or {@code null}, if team with {@code id} does not exist
     */
    public CodeInsightTeam getTeamById(Long id) {
        return teamMap.get(id);
    }

    /**
     * Returns sorted teams by {@code insideCodeMode}
     *
     * @param insideCodeMode mode, which decides by which field the teams are sorted
     * @return sorted teams by {@code insideCodeMode}, or {@code null} if non-supported {@code insideCodeMode} is provided
     */
    public List<CodeInsightTeam> getTeamsSorted(final CodeInsightService.InsideCodeMode insideCodeMode) {
        if (insideCodeMode == CodeInsightService.InsideCodeMode.DIFF) {
            return getTeamsSortedByDiffLines();
        }
        if (insideCodeMode == CodeInsightService.InsideCodeMode.COUNT) {
            return getTeamsSortedByCountLines();
        }
        return null;
    }

    private List<CodeInsightTeam> getTeamsSortedByDiffLines() {
        List<CodeInsightTeam> teams = new ArrayList<>(teamMap.values());
        Collections.sort(teams, new CodeInsightTeam.DiffLineCountComparator());
        return teams;
    }

    private List<CodeInsightTeam> getTeamsSortedByCountLines() {
        List<CodeInsightTeam> teams = new ArrayList<>(teamMap.values());
        Collections.sort(teams, new CodeInsightTeam.LineCountComparator());
        return teams;
    }
}
