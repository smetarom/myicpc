package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.*;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.eventFeed.*;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.scoreboard.eventFeed.dto.*;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class EventFeedVisitorImpl implements EventFeedVisitor {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedVisitorImpl.class);

    @Autowired
    private NativeRunStrategy nativeRunStrategy;

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

    @Autowired
    private EventFeedControlRepository eventFeedControlRepository;

    @Override
    @Transactional
    public void visit(final ContestXML xmlContest, Contest contest, EventFeedControl eventFeedControl) {
        contest = contestRepository.findOne(contest.getId());
        xmlContest.mergeTo(contest);
        // TODO remove timestamp, it is here for testing purposes
        contest.setStartTime(new Date());
        contest = contestRepository.saveAndFlush(contest);
    }

    @Override
    @Transactional
    public void visit(LanguageXML xmlLanguage, Contest contest, EventFeedControl eventFeedControl) {
        Language language = languageRepository.findByName(xmlLanguage.getName());
        if (language == null) {
            language = new Language();
            xmlLanguage.mergeTo(language);
            language = languageRepository.save(language);
            logger.info("Language " + language.getName() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(RegionXML xmlRegion, Contest contest, EventFeedControl eventFeedControl) {
        Region region = regionRepository.findByNameAndContest(xmlRegion.getName(), contest);
        if (region == null) {
            region = new Region();
            xmlRegion.mergeTo(region);
            region.setShortName(FormatUtils.getRegionShortName(region.getName()));
            region.setContest(contest);
            region = regionRepository.save(region);
            logger.info("Region " + region.getName() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(JudgementXML xmlJudgement, Contest contest, EventFeedControl eventFeedControl) {
        Judgement judgement = judgementRepository.findByCodeAndContest(xmlJudgement.getAcronym(), contest);
        if (judgement == null) {
            judgement = new Judgement();
            xmlJudgement.mergeTo(judgement);
            // TODO get color
//            judgement.setColor(GlobalUtils.getJudgementColor(judgement.getCode()));
            judgement.setContest(contest);
            judgement = judgementRepository.save(judgement);
            logger.info("Judgement " + judgement.getName() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(ProblemXML xmlProblem, Contest contest, EventFeedControl eventFeedControl) {
        Problem problem = problemRepository.findBySystemIdAndContest(xmlProblem.getSystemId(), contest);
        if (problem == null) {
            problem = new Problem();
            xmlProblem.mergeTo(problem);
            problem.setCode(Character.toString((char) (64 + problem.getSystemId())));
            problem.setContest(contest);
            problem = problemRepository.save(problem);
            logger.info("Problem " + problem.getCode() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(TeamXML xmlTeam, Contest contest, EventFeedControl eventFeedControl) {
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
            team = teamRepository.save(team);
            logger.info("Team " + team.getSystemId() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(TeamProblemXML xmlTeamProblem, Contest contest, EventFeedControl eventFeedControl) {
        try {
            if (eventFeedControl.getSkippedRuns() < eventFeedControl.getRunsToSkip()) {
                eventFeedControl.increaseSkippedRuns();
                logger.info("Skipped {} out of {} runs", eventFeedControl.getSkippedRuns(), eventFeedControl.getRunsToSkip());
            } else {
                FeedRunStrategy strategy = selectStrategy(contest);
                TeamProblem teamProblem = new TeamProblem();
                xmlTeamProblem.mergeTo(teamProblem);
                teamProblem = strategy.executeTeamProblem(teamProblem, contest);

                eventFeedControl = eventFeedControlRepository.findOne(eventFeedControl.getId());
                eventFeedControl.increaseProcessedRunsCounter();
                eventFeedControlRepository.save(eventFeedControl);
                logger.info("Run " + teamProblem.getSystemId() + " processed for team " + teamProblem.getTeam().getSystemId());
            }
        } catch (EventFeedException ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    @Override
    @Transactional
    public void visit(TestcaseXML xmlTestcase, Contest contest, EventFeedControl eventFeedControl) {
        TeamProblem teamProblem = teamProblemRepository.findBySystemIdAndTeamContest(xmlTestcase.getSystemId(), contest);
        if (teamProblem != null) {
            xmlTestcase.mergeTo(teamProblem);
        }
    }

    @Override
    @Transactional
    public void visit(AnalystMessageXML analystMessage, Contest contest, EventFeedControl eventFeedControl) {
        // TODO handle analyst message
    }

    @Override
    @Transactional
    public void visit(FinalizedXML finalizedXML, Contest contest, EventFeedControl eventFeedControl) {
        // TODO do something useful with finalized information
    }

    protected FeedRunStrategy selectStrategy(Contest contest) throws EventFeedException {
        switch (contest.getContestSettings().getScoreboardStrategyType()) {
            case NATIVE:
                return nativeRunStrategy;
            default:
                throw new EventFeedException("No suitable event feed strategy found.");
        }
    }

}
