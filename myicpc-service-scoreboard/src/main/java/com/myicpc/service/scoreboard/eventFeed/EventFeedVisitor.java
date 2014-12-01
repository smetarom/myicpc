package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.model.contest.Contest;
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
