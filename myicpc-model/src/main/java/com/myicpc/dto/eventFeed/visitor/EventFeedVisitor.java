package com.myicpc.dto.eventFeed.visitor;

import com.myicpc.dto.eventFeed.AnalystMessageXML;
import com.myicpc.dto.eventFeed.ContestXML;
import com.myicpc.dto.eventFeed.FinalizedXML;
import com.myicpc.dto.eventFeed.JudgementXML;
import com.myicpc.dto.eventFeed.LanguageXML;
import com.myicpc.dto.eventFeed.ProblemXML;
import com.myicpc.dto.eventFeed.RegionXML;
import com.myicpc.dto.eventFeed.TeamProblemXML;
import com.myicpc.dto.eventFeed.TeamXML;
import com.myicpc.dto.eventFeed.TestcaseXML;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.EventFeedControl;

public interface EventFeedVisitor {
    void visit(ContestXML c, Contest contest);

    void visit(LanguageXML language, Contest contest);

    void visit(RegionXML region, Contest contest);

    void visit(JudgementXML judgement, Contest contest);

    void visit(ProblemXML problem, Contest contest);

    void visit(TeamXML team, Contest contest);

    void visit(TeamProblemXML teamProblem, Contest contest);

    void visit(TestcaseXML testcase, Contest contest);

    void visit(AnalystMessageXML analystMessage, Contest contest);

    void visit(FinalizedXML finalizedXML, Contest contest);
}
