package com.myicpc.service.scoreboard.eventFeed.dto;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.service.scoreboard.eventFeed.EventFeedVisitor;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Roman Smetana
 */
@XStreamAlias("testcase")
public class TestcaseXML extends XMLEntity<TeamProblem> {
    /**
     * Submission ID
     */
    @XStreamAlias("run-id")
    private Long systemId;
    /**
     * Does submission passed all tests
     */
    private boolean solved;
    /**
     * Number of passed tests
     */
    @XStreamAlias("i")
    private int passedTests;
    /**
     * Number of failed tests
     */
    @XStreamAlias("n")
    private int totalTests;

    @Override
    public void mergeTo(final TeamProblem teamProblem) {
        teamProblem.setNumTestPassed(getPassedTests());
        teamProblem.setTotalNumTests(getTotalTests());
    }

    @Override
    public void accept(EventFeedVisitor visitor, Contest contest, EventFeedControl eventFeedControl) {
        visitor.visit(this, contest, eventFeedControl);
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public int getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(int passedTests) {
        this.passedTests = passedTests;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }
}
