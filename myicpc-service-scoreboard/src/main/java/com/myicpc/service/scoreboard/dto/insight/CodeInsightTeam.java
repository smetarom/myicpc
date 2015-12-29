package com.myicpc.service.scoreboard.dto.insight;

import com.myicpc.model.codeInsight.CodeInsightActivity;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.service.scoreboard.insight.CodeInsightService;
import com.myicpc.service.scoreboard.insight.CodeInsightService.InsideCodeMode;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Holds data for {@link Team} specific for Insight
 *
 * @author Roman Smetana
 */
public class CodeInsightTeam implements Serializable {
    private static final long serialVersionUID = 6777848135258336886L;
    /**
     * Team
     */
    private final Team team;
    /**
     * Total number of lines
     */
    private int lineCount;
    /**
     * Number of changed lines
     */
    private int diffLineCount;

    /**
     * Constructor
     *
     * @param codeInsightActivity insight activity
     * @param team
     */
    public CodeInsightTeam(CodeInsightActivity codeInsightActivity, Team team) {
        this.team = team;
        this.lineCount = codeInsightActivity.getLineCount();
        this.diffLineCount = codeInsightActivity.getDiffLineCount();
    }

    /**
     * Merges {@code codeInsightActivity} with already merged insight activities
     *
     * @param codeInsightActivity insight activity
     */
    public void merge(CodeInsightActivity codeInsightActivity) {
        lineCount = Math.max(lineCount, codeInsightActivity.getLineCount());
        diffLineCount += codeInsightActivity.getDiffLineCount();
    }

    public Team getTeam() {
        return team;
    }

    public String getTeamLabel() {
        return team.getName();
    }

    /**
     * Returns data used in charts based on {@code insideCodeMode}
     *
     * @param insideCodeMode aspect you are interested in
     * @return specified aspect of {@code this}
     */
    public int getChartData(final InsideCodeMode insideCodeMode) {
        if (insideCodeMode == InsideCodeMode.DIFF) {
            return diffLineCount;
        }
        if (insideCodeMode == InsideCodeMode.COUNT) {
            return lineCount;
        }
        return 0;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getDiffLineCount() {
        return diffLineCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodeInsightTeam that = (CodeInsightTeam) o;

        if (team != null ? !team.equals(that.team) : that.team != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return team != null ? team.hashCode() : 0;
    }

    /**
     * Sort by total number of lines
     *
     * @author Roman Smetana
     */
    public static class LineCountComparator implements Comparator<CodeInsightTeam>, Serializable {
        private static final long serialVersionUID = -514019705904152192L;

        @Override
        public int compare(CodeInsightTeam o1, CodeInsightTeam o2) {
            int diff = -1 * Integer.valueOf(o1.lineCount).compareTo(o2.lineCount);
            if (diff == 0 && o1.team != null && o1.team.getSystemId() != null && o2.team != null && o2.team.getSystemId() != null) {
                return o1.team.getSystemId().compareTo(o2.team.getSystemId());
            } else {
                return diff;
            }
        }
    }

    /**
     * Sort by number of changed lines
     *
     * @author Roman Smetana
     */
    public static class DiffLineCountComparator implements Comparator<CodeInsightTeam>, Serializable {
        private static final long serialVersionUID = 9114253069116582829L;

        @Override
        public int compare(final CodeInsightTeam o1, final CodeInsightTeam o2) {
            int diff = -1 * Integer.valueOf(o1.diffLineCount).compareTo(o2.diffLineCount);
            if (diff == 0 && o1.team != null && o1.team.getSystemId() != null && o2.team != null && o2.team.getSystemId() != null) {
                return o1.team.getSystemId().compareTo(o2.team.getSystemId());
            } else {
                return diff;
            }
        }
    }
}
