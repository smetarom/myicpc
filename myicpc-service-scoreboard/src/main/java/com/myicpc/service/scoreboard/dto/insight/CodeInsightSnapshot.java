package com.myicpc.service.scoreboard.dto.insight;

import java.io.Serializable;
import java.util.Map;

/**
 * Snapshot of insight problem states at a time
 *
 * @author Roman Smetana
 */
public class CodeInsightSnapshot implements Serializable {
    /**
     * Contest time in minutes
     */
    private final long time;
    /**
     * Map between problem ID and {@link CodeInsightProblem}
     */
    private final Map<String, CodeInsightProblem> problems;

    /**
     * Constructor
     *
     * @param time contest time in minutes
     * @param problems map between problem ID and {@link CodeInsightProblem}
     */
    public CodeInsightSnapshot(long time, Map<String, CodeInsightProblem> problems) {
        this.time = time;
        this.problems = problems;
    }

    public long getTime() {
        return time;
    }

    /**
     * Gets {@link CodeInsightProblem} by problem code
     *
     * @param problemCode problem code
     * @return {@link CodeInsightProblem} by problem code, or {@code null} if it does not exist
     */
    public CodeInsightProblem getProblemByCode(String problemCode) {
        return problems.get(problemCode);
    }
}
