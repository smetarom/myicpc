package com.myicpc.service.scoreboard.dto.insight;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data holder for insight dashboard
 *
 * @author Roman Smetana
 */
public class DashboardInsightDTO implements Serializable {
    private static final long serialVersionUID = -8088797654035344560L;

    private List<DashboardResult> currentProblemResult = new ArrayList<>();
    private List<DashboardResult> currentTeamResult = new ArrayList<>();

    public List<DashboardResult> getCurrentProblemResult() {
        return currentProblemResult;
    }

    public List<DashboardResult> getCurrentTeamResult() {
        return currentTeamResult;
    }

    public void setCurrentTeamResult(List<DashboardResult> currentTeamResult) {
        this.currentTeamResult = currentTeamResult;
    }

    public static class DashboardResult implements Serializable {
        private static final long serialVersionUID = 1532140615470599008L;

        private String label;
        private int solved;
        private int submitted;

        public static DashboardResult emptyResult(String label) {
            return new DashboardResult(label, 0, 0);
        }

        public DashboardResult(String label, int solved, int submitted) {
            this.label = label;
            this.solved = solved;
            this.submitted = submitted;
        }

        public String getLabel() {
            return label;
        }

        public int getSolved() {
            return solved;
        }

        public int getSubmitted() {
            return submitted;
        }
    }
}
