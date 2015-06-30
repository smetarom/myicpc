package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.commons.utils.FormatUtils;
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
import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Judgement;
import com.myicpc.model.eventFeed.Language;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Region;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.eventFeed.JudgementRepository;
import com.myicpc.repository.eventFeed.LanguageRepository;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.RegionRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.scoreboard.eventFeed.strategy.FeedRunStrategy;
import com.myicpc.service.scoreboard.eventFeed.strategy.JSONRunStrategy;
import com.myicpc.service.scoreboard.eventFeed.strategy.NativeRunStrategy;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventFeedVisitorImpl implements EventFeedVisitor {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedVisitorImpl.class);
    private static final Map<Long, TestcaseXML> testcaseXMLMap = new ConcurrentHashMap<>();

    @Autowired
    private NativeRunStrategy nativeRunStrategy;

    @Autowired
    private JSONRunStrategy jsonRunStrategy;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private JudgementRepository judgementRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamInfoRepository teamInfoRepository;

    @Autowired
    private TeamProblemRepository teamProblemRepository;

    @Override
    @Transactional
    public void visit(final ContestXML xmlContest, Contest contest) {
        contest = contestRepository.findOne(contest.getId());
        xmlContest.mergeTo(contest);
        // TODO remove timestamp, it is here for testing purposes
        contest.setStartTime(new Date());
        contestRepository.saveAndFlush(contest);
    }

    @Override
    @Transactional
    public void visit(LanguageXML xmlLanguage, Contest contest) {
        Language language = languageRepository.findByName(xmlLanguage.getName());
        if (language == null) {
            language = new Language();
            xmlLanguage.mergeTo(language);
            language = languageRepository.saveAndFlush(language);
            logger.info("Language " + language.getName() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(RegionXML xmlRegion, Contest contest) {
        Region region = regionRepository.findByNameAndContest(xmlRegion.getName(), contest);
        if (region == null) {
            region = new Region();
            xmlRegion.mergeTo(region);
            region.setShortName(FormatUtils.getRegionShortName(region.getName()));
            region.setContest(contest);
            region = regionRepository.saveAndFlush(region);
            logger.info("Region " + region.getName() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(JudgementXML xmlJudgement, Contest contest) {
        Judgement judgement = judgementRepository.findByCodeAndContest(xmlJudgement.getAcronym(), contest);
        if (judgement == null) {
            judgement = new Judgement();
            xmlJudgement.mergeTo(judgement);
            // TODO get color
//            judgement.setColor(GlobalUtils.getJudgementColor(judgement.getCode()));
            judgement.setContest(contest);
            judgement = judgementRepository.saveAndFlush(judgement);
            logger.info("Judgement " + judgement.getName() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(ProblemXML xmlProblem, Contest contest) {
        Problem problem = problemRepository.findBySystemIdAndContest(xmlProblem.getSystemId(), contest);
        if (problem == null) {
            problem = new Problem();
            xmlProblem.mergeTo(problem);
            problem.setCode(Character.toString((char) (64 + problem.getSystemId())));
            problem.setContest(contest);
            problem = problemRepository.saveAndFlush(problem);
            logger.info("Problem " + problem.getCode() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(TeamXML xmlTeam, Contest contest) {
        Team team = teamRepository.findBySystemIdAndContest(xmlTeam.getId(), contest);
        if (team == null) {
            team = new Team();
            xmlTeam.mergeTo(team);
            team.setRank(1);
            team.setProblemsSolved(0);
            team.setTotalTime(0);
            team.setContest(contest);
            TeamInfo teamInfo = teamInfoRepository.findByExternalIdAndContest(team.getExternalId(), contest);
            team.setTeamInfo(teamInfo);
            team = teamRepository.saveAndFlush(team);
            logger.info("Team " + team.getSystemId() + " created");
        }
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void visit(TeamProblemXML xmlTeamProblem, Contest contest) {
        // TODO Consider remove JOIN team from query
        Boolean judged = teamProblemRepository.getJudgedBySystemIdAndTeamContest(xmlTeamProblem.getSystemId(), contest);
        if (judged != null) {
            if ("fresh".equalsIgnoreCase(xmlTeamProblem.getStatus())) {
                logger.info("Skip 'fresh' submission {} from team {}", xmlTeamProblem.getSystemId(), xmlTeamProblem.getTeamId());
                return;
            }
            if ("done".equalsIgnoreCase(xmlTeamProblem.getStatus()) && judged) {
                logger.info("Skip 'done' submission {} from team {}", xmlTeamProblem.getSystemId(), xmlTeamProblem.getTeamId());
                return;
            }
        }
        TeamProblem teamProblem = teamProblemRepository.findBySystemIdAndTeamContest(xmlTeamProblem.getSystemId(), contest);

        try {
            FeedRunStrategy strategy = selectStrategy(contest);
            if (teamProblem == null) {
                teamProblem = new TeamProblem();
            }
            xmlTeamProblem.mergeTo(teamProblem);
            if (teamProblem.getTeam() == null) {
                teamProblem.setTeam(teamRepository.findBySystemIdAndContest(xmlTeamProblem.getTeamId(), contest));
            }
            if (teamProblem.getProblem() == null) {
                teamProblem.setProblem(problemRepository.findBySystemIdAndContest(xmlTeamProblem.getProblemId(), contest));
            }
            TestcaseXML testcaseXML = testcaseXMLMap.get(teamProblem.getSystemId());
            if (testcaseXML != null) {
                teamProblem.setNumTestPassed(testcaseXML.getPassedTests());
                teamProblem.setTotalNumTests(testcaseXML.getTotalTests());
                teamProblem.getProblem().setTotalTestcases(testcaseXML.getTotalTests());
            }
            teamProblem = strategy.executeTeamProblem(teamProblem, contest);

            logger.info("Run " + teamProblem.getSystemId() + " processed for team " + teamProblem.getTeam().getSystemId());
        } catch (EventFeedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public void visit(TestcaseXML xmlTestcase, Contest contest) {
        testcaseXMLMap.put(xmlTestcase.getSystemId(), xmlTestcase);
    }

    @Override
    @Transactional
    public void visit(AnalystMessageXML analystMessage, Contest contest) {
        // TODO handle analyst message
    }

    @Override
    @Transactional
    public void visit(FinalizedXML finalizedXML, Contest contest) {
        // TODO do something useful with finalized information
    }

    protected FeedRunStrategy selectStrategy(Contest contest) throws EventFeedException {
        switch (contest.getContestSettings().getScoreboardStrategyType()) {
            case NATIVE:
                return nativeRunStrategy;
            case JSON:
                return jsonRunStrategy;
            default:
                throw new EventFeedException("No suitable event feed strategy found.");
        }
    }

}
