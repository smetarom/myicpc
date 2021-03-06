package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.dto.eventFeed.parser.AnalystMessageXML;
import com.myicpc.dto.eventFeed.parser.ContestXML;
import com.myicpc.dto.eventFeed.parser.EventFeedSettingsDTO;
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
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.model.eventFeed.Judgement;
import com.myicpc.model.eventFeed.JudgementColor;
import com.myicpc.model.eventFeed.Language;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.social.Notification;
import com.myicpc.model.teamInfo.Region;
import com.myicpc.model.teamInfo.TeamInfo;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.eventFeed.EventFeedControlRepository;
import com.myicpc.repository.eventFeed.JudgementColorRepository;
import com.myicpc.repository.eventFeed.JudgementRepository;
import com.myicpc.repository.eventFeed.LanguageRepository;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.TeamProblemRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.repository.teamInfo.TeamInfoRepository;
import com.myicpc.service.publish.PublishService;
import com.myicpc.service.scoreboard.eventFeed.strategy.FeedRunStrategy;
import com.myicpc.service.scoreboard.eventFeed.strategy.JSONRunStrategy;
import com.myicpc.service.scoreboard.eventFeed.strategy.NativeRunStrategy;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of {@link EventFeedVisitor}
 *
 * @author Roman Smetana
 */
@Service
public class EventFeedVisitorImpl implements EventFeedVisitor {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedVisitorImpl.class);
    private static final Map<Long, TestcaseXML> testcaseXMLMap = new ConcurrentHashMap<>();

    @Autowired
    private PublishService publishService;

    @Autowired
    private NativeRunStrategy nativeRunStrategy;

    @Autowired
    private JSONRunStrategy jsonRunStrategy;

    @Autowired
    private AnalystMessageService analystMessageService;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private JudgementRepository judgementRepository;

    @Autowired
    private JudgementColorRepository judgementColorRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamInfoRepository teamInfoRepository;

    @Autowired
    private TeamProblemRepository teamProblemRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EventFeedControlRepository eventFeedControlRepository;

    @Override
    @Transactional
    public void visit(final ContestXML xmlContest, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        contest = contestRepository.findOne(contest.getId());
        xmlContest.mergeTo(contest);
        // TODO remove timestamp, it is here for testing purposes
//        contest.setStartTime(new Date());
        contestRepository.saveAndFlush(contest);
    }

    @Override
    @Transactional
    public void visit(LanguageXML xmlLanguage, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        Language language = languageRepository.findByNameAndContest(xmlLanguage.getName(), contest);
        if (language == null) {
            language = new Language();
            xmlLanguage.mergeTo(language);
            language.setContest(contest);
            language = languageRepository.saveAndFlush(language);
            logger.info("Language " + language.getName() + " created");
        }
    }

    @Override
    public void visit(RegionXML xmlRegion, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        // Ignore regions from the feed
    }

    @Override
    @Transactional
    public void visit(JudgementXML xmlJudgement, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        Judgement judgement = judgementRepository.findByCodeAndContest(xmlJudgement.getAcronym(), contest);
        if (judgement == null) {
            judgement = new Judgement();
            xmlJudgement.mergeTo(judgement);
            JudgementColor judgementColor = judgementColorRepository.findByCodeAndContest(xmlJudgement.getAcronym(), contest);
            judgement.setColor(getJudgmentColor(judgementColor, xmlJudgement.getAcronym()));
            judgement.setContest(contest);
            judgement = judgementRepository.saveAndFlush(judgement);
            logger.info("Judgement " + judgement.getName() + " created");
        }
    }

    @Override
    @Transactional
    public void visit(ProblemXML xmlProblem, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
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
    public void visit(TeamXML xmlTeam, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        Team team = teamRepository.findBySystemIdAndContest(xmlTeam.getId(), contest);
        if (team == null) {
            team = new Team();
            xmlTeam.mergeTo(team);
            team.setRank(1);
            team.setProblemsSolved(0);
            team.setTotalTime(0);
            team.setContest(contest);
            TeamInfo teamInfo = teamInfoRepository.findByExternalIdAndContestWithRegion(team.getExternalId(), contest);
            team.setTeamInfo(teamInfo);
            if (teamInfo != null && teamInfo.getRegion() != null) {
                Region region = teamInfo.getRegion();
                if (region.getRegionType() != Region.RegionType.UNOFFICIAL) {
                    team = teamRepository.saveAndFlush(team);
                    logger.info("Team " + team.getSystemId() + " created");
                }
            } else {
                team = teamRepository.saveAndFlush(team);
                logger.info("Team " + team.getSystemId() + " created");
            }
        }
    }

    @Override
    @Transactional
    public void visit(TeamProblemXML xmlTeamProblem, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        EventFeedControl submissionFeedControl = null;
        if (ControlFeedService.hasContestPollingStrategy(contest)) {
            submissionFeedControl = eventFeedControlRepository.findByContest(contest);
            if (submissionFeedControl == null) {
                submissionFeedControl = new EventFeedControl();
                submissionFeedControl.setContest(contest);
            }
            if (xmlTeamProblem.getSubmissionOrder() < submissionFeedControl.getProcessedRunsCounter()) {
                logger.warn(contest.getCode() + ": Run " + xmlTeamProblem.getSystemId() + " for team " + xmlTeamProblem.getTeamId() + " was skipped.");
                return;
            }
        }
        // TODO Consider remove JOIN team from query
        Boolean judged = teamProblemRepository.getJudgedBySystemIdAndTeamContest(xmlTeamProblem.getSystemId(), contest);
        if (judged != null) {
            if ("fresh".equalsIgnoreCase(xmlTeamProblem.getStatus())) {
                logger.info(contest.getCode() + ": Skip 'fresh' submission {} from team {}", xmlTeamProblem.getSystemId(), xmlTeamProblem.getTeamId());
                return;
            }
            if ("done".equalsIgnoreCase(xmlTeamProblem.getStatus()) && judged) {
                logger.info(contest.getCode() + ": Skip 'done' submission {} from team {}", xmlTeamProblem.getSystemId(), xmlTeamProblem.getTeamId());
                return;
            }
        }
        if (teamProblemRepository.countByTeamContest(contest) == 0) {
            publishService.broadcastEventFeedRefresh(contest.getCode());
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
            if (teamProblem.getTeam() == null || teamProblem.getProblem() == null) {
                // team not found in the contest -> ignore the submission
                return;
            }
            TestcaseXML testcaseXML = testcaseXMLMap.get(teamProblem.getSystemId());
            if (testcaseXML != null) {
                teamProblem.setNumTestPassed(testcaseXML.getPassedTests());
                teamProblem.setTotalNumTests(testcaseXML.getTotalTests());
                teamProblem.getProblem().setTotalTestcases(testcaseXML.getTotalTests());
            }
            teamProblem = strategy.executeTeamProblem(teamProblem, contest, eventFeedSettings);

            if (ControlFeedService.hasContestPollingStrategy(contest) && submissionFeedControl != null) {
                submissionFeedControl.increaseProcessedRunsCounter();
                eventFeedControlRepository.save(submissionFeedControl);
            }

            logger.info(contest.getCode() + ": Run " + teamProblem.getSystemId() + " processed for team " + teamProblem.getTeam().getSystemId());
        } catch (EventFeedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public void visit(TestcaseXML xmlTestcase, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        testcaseXMLMap.put(xmlTestcase.getSystemId(), xmlTestcase);
    }

    @Override
    @Transactional
    public void visit(AnalystMessageXML analystMessage, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
        if (eventFeedSettings.isSkipMessageGeneration()) {
            // skip, if we don't generate any info messages
            return;
        }
        if (StringUtils.isEmpty(analystMessage.getMessage())) {
            // skip, if the message is empty
            return;
        }
        Notification notification = null;
        if (analystMessage.getRunId() != null) { // notification for submission
            TeamProblem teamProblem = teamProblemRepository.findBySystemIdAndTeamContest(analystMessage.getRunId(), contest);
            // if team submission exists and message does not exist
            if (teamProblem != null
                    && notificationRepository.countAnalystSubmissionMessage(teamProblem.getId(), analystMessage.getMessage(), contest) == 0) {
                if (teamProblem.getJudged()) {
                    if (teamProblem.getSolved()) {
                        notification = analystMessageService.createOnTeamSubmissionSuccess(teamProblem, analystMessage.getMessage());
                    } else {
                        notification = analystMessageService.createOnTeamSubmissionFailed(teamProblem, analystMessage.getMessage());
                    }
                } else {
                    notification = analystMessageService.createOnTeamSubmissionSubmitted(teamProblem, analystMessage.getMessage());
                }
            }
        } else if (analystMessage.getTeamId() != null) { // notification for a team
            Team team = teamRepository.findBySystemIdAndContest(analystMessage.getTeamId(), contest);
            if (team != null
                    && notificationRepository.countAnalystTeamMessage(team.getId(), analystMessage.getMessage(), contest) == 0) {
                notification = analystMessageService.createTeamAnalyticsMessage(team, analystMessage.getMessage());
            }
        } else { // general analytics notification
            if (notificationRepository.countAnalystGeneralMessage(analystMessage.getMessage(), contest) == 0) {
                Contest msgContest = contestRepository.findOne(contest.getId());
                notification = analystMessageService.createAnalyticsMessage(msgContest, analystMessage.getMessage());
            }
        }

        if (notification != null) {
            notificationRepository.save(notification);
            publishService.broadcastNotification(notification, notification.getContest());
        }
    }

    @Override
    @Transactional
    public void visit(FinalizedXML finalizedXML, Contest contest, EventFeedSettingsDTO eventFeedSettings) {
    }

    private FeedRunStrategy selectStrategy(Contest contest) throws EventFeedException {
        switch (contest.getContestSettings().getScoreboardStrategyType()) {
            case NATIVE:
                return nativeRunStrategy;
            case JSON:
                return jsonRunStrategy;
            case POLLING:
                return nativeRunStrategy;
            default:
                throw new EventFeedException("No suitable event feed strategy found.");
        }
    }

    private static String getJudgmentColor(final JudgementColor judgementColor, String judgementCode) {
        if (judgementColor != null) {
            return judgementColor.getColor();
        }
        return JudgementColor.getDefaultColor(judgementCode);
    }

}
