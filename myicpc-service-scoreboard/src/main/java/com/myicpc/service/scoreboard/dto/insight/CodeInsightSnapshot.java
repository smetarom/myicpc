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
    private final Map<Long, CodeInsightProblem> problems;

    /**
     * Constructor
     *
     * @param time contest time in minutes
     * @param problems map between problem ID and {@link CodeInsightProblem}
     */
    public CodeInsightSnapshot(long time, Map<Long, CodeInsightProblem> problems) {
        this.time = time;
        this.problems = problems;
    }

    public long getTime() {
        return time;
    }

    /**
     * Gets {@link CodeInsightProblem} by problem ID
     *
     * @param id problem ID
     * @return {@link CodeInsightProblem} by problem ID, or {@code null} if it does not exist
     */
    public CodeInsightProblem getProblemById(Long id) {
        return problems.get(id);
    }
}
