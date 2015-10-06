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
import com.myicpc.model.eventFeed.Judgement;
import com.myicpc.model.eventFeed.Language;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.social.Notification;
import com.myicpc.model.teamInfo.Region;

/**
 * Defines actions taken on each XML tag from CDS Event feed
 *
 * @author Roman Smetana
 */
public interface EventFeedVisitor {
    /**
     * Triggered on &lt;info&gt; tag
     *
     * Updates {@link Contest} info from the feed
     *
     * @param c contest from the feed
     * @param contest contest
     */
    void visit(ContestXML c, Contest contest);

    /**
     * Triggered on &lt;language&gt; tag
     *
     * Updates {@link Language} from the feed
     *
     * @param language language from the feed
     * @param contest contest
     */
    void visit(LanguageXML language, Contest contest);

    /**
     * Triggered on &lt;region&gt; tag
     *
     * Updates {@link Region} from the feed
     *
     * @param region region from the feed
     * @param contest contest
     */
    void visit(RegionXML region, Contest contest);

    /**
     * Triggered on &lt;judgement&gt; tag
     *
     * Updates {@link Judgement} from the feed
     *
     * @param judgement judgement from the feed
     * @param contest contest
     */
    void visit(JudgementXML judgement, Contest contest);

    /**
     * Triggered on &lt;problem&gt; tag
     *
     * Updates {@link Problem} from the feed
     *
     * @param problem problem from the feed
     * @param contest contest
     */
    void visit(ProblemXML problem, Contest contest);

    /**
     * Triggered on &lt;team&gt; tag
     *
     * Updates {@link Team} from the feed
     *
     * @param team team from the feed
     * @param contest contest
     */
    void visit(TeamXML team, Contest contest);

    /**
     * Triggered on &lt;run&gt; tag
     *
     * Updates {@link TeamProblem} from the feed
     *
     * @param teamProblem team from the feed
     * @param contest contest
     */
    void visit(TeamProblemXML teamProblem, Contest contest);

    /**
     * Triggered on &lt;testcase&gt; tag
     *
     * Processes the testcase
     *
     * @param testcase team from the feed
     * @param contest contest
     */
    void visit(TestcaseXML testcase, Contest contest);

    /**
     * Triggered on &lt;analystmsg&gt; tag
     *
     * Creates a {@link Notification} from {@link AnalystMessageXML}
     *
     * @param analystMessage team from the feed
     * @param contest contest
     */
    void visit(AnalystMessageXML analystMessage, Contest contest);

    /**
     * Triggered on &lt;finalized&gt; tag
     *
     * Finalizes the contest with information from {@link FinalizedXML}
     *
     * @param finalizedXML finalize information from the feed
     * @param contest contest
     */
    void visit(FinalizedXML finalizedXML, Contest contest);
}
