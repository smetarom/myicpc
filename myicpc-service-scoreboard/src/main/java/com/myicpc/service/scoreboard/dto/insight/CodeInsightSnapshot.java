package com.myicpc.service.scoreboard.dto.insight;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Roman Smetana
 */
public class CodeInsightSnapshot implements Serializable {
    private final int time;
    private final Map<Long, CodeInsightProblem> problems;

    public CodeInsightSnapshot(int time, Map<Long, CodeInsightProblem> problems) {
        this.time = time;
        this.problems = problems;
    }

    public int getTime() {
        return time;
    }

    public CodeInsightProblem getProblemById(Long id) {
        return problems.get(id);
    }
}
