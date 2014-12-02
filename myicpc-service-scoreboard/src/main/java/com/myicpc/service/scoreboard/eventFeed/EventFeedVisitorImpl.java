package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.commons.utils.TextUtils;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.*;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.eventFeed.*;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.scoreboard.eventFeed.dto.*;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    @Transactional
    public void visit(final ContestXML xmlContest, Contest contest) {
        contest = contestRepository.findOne(contest.getId());
        xmlContest.mergeTo(contest);
        // TODO remove timestamp, it is here for testing purposes
        contest.setStartTime(new Date());
        contest = contestRepository.saveAndFlush(contest);
    }

    @Override
    @Transactional
    public void visit(LanguageXML xmlLanguage, Contest contest) {
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
    public void visit(RegionXML xmlRegion, Contest contest) {
        Region region = regionRepository.findByNameAndContest(xmlRegion.getName(), contest);
        if (region == null) {
            region = new Region();
            xmlRegion.mergeTo(region);
            region.setShortName(TextUtils.getRegionShortName(region.getName()));
            region.setContest(contest);
            region = regionRepository.save(region);
            logger.info("Region " + region.getName() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(JudgementXML xmlJudgement, Contest contest) {
        Judgement judgement = judgementRepository.findByCode(xmlJudgement.getAcronym());
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
    public void visit(ProblemXML xmlProblem, Contest contest) {
        Problem problem = problemRepository.findByCodeAndContest(xmlProblem.getCode(), contest);
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
    public void visit(TeamXML xmlTeam, Contest contest) {
        Team team = teamRepository.findBySystemIdAndContest(xmlTeam.getId(), contest);
        if (team == null) {
            team = new Team();
            xmlTeam.mergeTo(team);
            team.setShortName(TextUtils.getTeamShortName(xmlTeam.getName()));
            team.setRank(1);
            team.setProblemsSolved(0);
            team.setTotalTime(0);
            team.setContest(contest);
            TeamInfo teamInfo = teamInfoRepository.findByExternalIdAndContest(team.getExternalId(), contest);
            if (teamInfo != null) {
                team.setAbbreviation(teamInfo.getAbbreviation());
                team.setHashtag(teamInfo.getHashtag());
            }
            if (StringUtils.isEmpty(team.getAbbreviation())) {
                team.setAbbreviation(team.getShortName());
            }
            team = teamRepository.save(team);
            logger.info("Team " + team.getSystemId() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(TeamProblemXML xmlTeamProblem, Contest contest) {
        try {
            FeedRunStrategy strategy = selectStrategy(contest);
            TeamProblem teamProblem = new TeamProblem();
            xmlTeamProblem.mergeTo(teamProblem);
            teamProblem = strategy.executeTeamProblem(teamProblem, contest);
            logger.info("Run " + teamProblem.getSystemId() + " processed for team " + teamProblem.getTeam().getSystemId());
        } catch (EventFeedException ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    @Override
    @Transactional
    public void visit(TestcaseXML xmlTestcase, Contest contest) {
        TeamProblem teamProblem = teamProblemRepository.findBySystemIdAndTeamContest(xmlTestcase.getSystemId(), contest);
        if (teamProblem != null) {
            xmlTestcase.mergeTo(teamProblem);
        }
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
            default:
                throw new EventFeedException("No suitable event feed strategy found.");
        }
    }

}
