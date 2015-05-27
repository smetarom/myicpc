package com.myicpc.dto.eventFeed.visitor;

import com.myicpc.dto.eventFeed.parser.AnalystMessageXML;
import com.myicpc.dto.eventFeed.parser.ContestXML;
import com.myicpc.dto.eventFeed.parser.FinalizedXML;
import com.myicpc.dto.eventFeed.parser.JudgementXML;
import com.myicpc.dto.eventFeed.parser.LanguageXML;
import com.myicpc.dto.eventFeed.parser.ProblemXML;
import com.myicpc.dto.eventFeed.parser.RegionXML;
import com.myicpc.dto.eventFeed.parser.TeamProblemXML;
import com.myicpc.dto.eventFeed.parser.TeamXML;
import com.myicpc.dto.eventFeed.parser.TestcaseXML;
import com.myicpc.model.contest.Contest;

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
