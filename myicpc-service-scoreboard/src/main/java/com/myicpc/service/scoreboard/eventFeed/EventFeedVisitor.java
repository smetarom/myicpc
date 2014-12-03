package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.service.scoreboard.eventFeed.dto.AnalystMessageXML;
import com.myicpc.service.scoreboard.eventFeed.dto.ContestXML;
import com.myicpc.service.scoreboard.eventFeed.dto.FinalizedXML;
import com.myicpc.service.scoreboard.eventFeed.dto.JudgementXML;
import com.myicpc.service.scoreboard.eventFeed.dto.LanguageXML;
import com.myicpc.service.scoreboard.eventFeed.dto.ProblemXML;
import com.myicpc.service.scoreboard.eventFeed.dto.RegionXML;
import com.myicpc.service.scoreboard.eventFeed.dto.TeamProblemXML;
import com.myicpc.service.scoreboard.eventFeed.dto.TeamXML;
import com.myicpc.service.scoreboard.eventFeed.dto.TestcaseXML;

public interface EventFeedVisitor {
    void visit(ContestXML c, Contest contest, EventFeedControl eventFeedControl);

    void visit(LanguageXML language, Contest contest, EventFeedControl eventFeedControl);

    void visit(RegionXML region, Contest contest, EventFeedControl eventFeedControl);

    void visit(JudgementXML judgement, Contest contest, EventFeedControl eventFeedControl);

    void visit(ProblemXML problem, Contest contest, EventFeedControl eventFeedControl);

    void visit(TeamXML team, Contest contest, EventFeedControl eventFeedControl);

    void visit(TeamProblemXML teamProblem, Contest contest, EventFeedControl eventFeedControl);

    void visit(TestcaseXML testcase, Contest contest, EventFeedControl eventFeedControl);

    void visit(AnalystMessageXML analystMessage, Contest contest, EventFeedControl eventFeedControl);

    void visit(FinalizedXML finalizedXML, Contest contest, EventFeedControl eventFeedControl);
}
