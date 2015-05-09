package com.myicpc.master.service.scoreboard;

import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.dto.eventFeed.AnalystMessageXML;
import com.myicpc.dto.eventFeed.ContestXML;
import com.myicpc.dto.eventFeed.EventFeedEvent;
import com.myicpc.dto.eventFeed.EventFeedSubmission;
import com.myicpc.dto.eventFeed.FinalizedXML;
import com.myicpc.dto.eventFeed.JudgementXML;
import com.myicpc.dto.eventFeed.LanguageXML;
import com.myicpc.dto.eventFeed.ProblemXML;
import com.myicpc.dto.eventFeed.RegionXML;
import com.myicpc.dto.eventFeed.TeamProblemXML;
import com.myicpc.dto.eventFeed.TeamXML;
import com.myicpc.dto.eventFeed.TestcaseXML;
import com.myicpc.dto.eventFeed.visitor.EventFeedMessage;
import com.myicpc.master.dao.EventFeedDao;
import com.myicpc.master.dao.NotificationDao;
import com.myicpc.master.exception.EventFeedException;
import com.myicpc.master.service.NotificationService;
import com.myicpc.master.service.scoreboard.strategy.FeedRunStrategy;
import com.myicpc.master.service.scoreboard.strategy.JSONRunStrategy;
import com.myicpc.master.service.scoreboard.strategy.NativeRunStrategy;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.eventFeed.Judgement;
import com.myicpc.model.eventFeed.Language;
import com.myicpc.model.eventFeed.Problem;
import com.myicpc.model.eventFeed.Region;
import com.myicpc.model.eventFeed.Team;
import com.myicpc.model.eventFeed.TeamProblem;
import com.myicpc.model.social.Notification;
import com.myicpc.model.teamInfo.TeamInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import java.util.Date;

/**
 * @author Roman Smetana
 */
@Stateless
@Local(EventFeedLocal.class)
public class EventFeedVisitorImpl implements EventFeedLocal {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedVisitorImpl.class);


    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/EventFeedQueue")
    private Queue eventFeedQueue;

    @Inject
    private EventFeedDao eventFeedDao;

    @Inject
    private NotificationDao notificationDao;

    @Inject
    private NativeRunStrategy nativeRunStrategy;

    @Inject
    private JSONRunStrategy jsonRunStrategy;

    @Inject
    private NotificationService notificationService;

    @Override
    public void visit(ContestXML xmlContest, Contest contest) {
        contest = eventFeedDao.findContestById(contest.getId());
        xmlContest.mergeTo(contest);
        // TODO remove timestamp, it is here for testing purposes
        contest.setStartTime(new Date());
        eventFeedDao.saveContestEntity(contest);
    }

    @Override
    public void visit(LanguageXML xmlLanguage, Contest contest) {
        Language language = eventFeedDao.findLanguageByName(xmlLanguage.getName());
        if (language == null) {
            language = new Language();
            xmlLanguage.mergeTo(language);
            language = eventFeedDao.saveContestEntity(language);
            logger.info("Language " + language.getName() + " created");
        }
    }

    @Override
    public void visit(RegionXML xmlRegion, Contest contest) {
        contest = eventFeedDao.findContestById(contest.getId());
        Region region = eventFeedDao.findRegionByNameAndContest(xmlRegion.getName(), contest);
        if (region == null) {
            region = new Region();
            xmlRegion.mergeTo(region);
            region.setShortName(FormatUtils.getRegionShortName(region.getName()));
            region.setContest(contest);
            region = eventFeedDao.saveContestEntity(region);
            logger.info("Region " + region.getName() + " created");
        }
    }

    @Override
    public void visit(JudgementXML xmlJudgement, Contest contest) {
        contest = eventFeedDao.findContestById(contest.getId());
        Judgement judgement = eventFeedDao.findJudgementByCodeAndContest(xmlJudgement.getAcronym(), contest);
        if (judgement == null) {
            judgement = new Judgement();
            xmlJudgement.mergeTo(judgement);
            // TODO get color
//            judgement.setColor(GlobalUtils.getJudgementColor(judgement.getCode()));
            judgement.setContest(contest);
            judgement = eventFeedDao.saveContestEntity(judgement);
            logger.info("Judgement " + judgement.getName() + " created");
        }
    }

    @Override
    public void visit(ProblemXML xmlProblem, Contest contest) {
        contest = eventFeedDao.findContestById(contest.getId());
        Problem problem = eventFeedDao.findProblemBySystemIdAndContest(xmlProblem.getSystemId(), contest);
        if (problem == null) {
            problem = new Problem();
            xmlProblem.mergeTo(problem);
            problem.setCode(Character.toString((char) (64 + problem.getSystemId())));
            problem.setContest(contest);
            problem = eventFeedDao.saveContestEntity(problem);
            logger.info("Problem " + problem.getCode() + " created");
        }
    }

    @Override
    public void visit(TeamXML xmlTeam, Contest contest) {
        contest = eventFeedDao.findContestById(contest.getId());
        Team team = eventFeedDao.findTeamBySystemIdAndContest(xmlTeam.getId(), contest);
        if (team == null) {
            team = new Team();
            xmlTeam.mergeTo(team);
            team.setRank(1);
            team.setProblemsSolved(0);
            team.setTotalTime(0);
            team.setContest(contest);
            TeamInfo teamInfo = eventFeedDao.findTeamInfoByExternalIdAndContest(team.getExternalId(), contest);
            team.setTeamInfo(teamInfo);
            team = eventFeedDao.saveContestEntity(team);
            logger.info("Team " + team.getSystemId() + " created");
        }
    }

    @Override
    public void visit(TeamProblemXML xmlTeamProblem, Contest contest) {
        TeamProblem teamProblem = eventFeedDao.findTeamProblemBySystemIdAndTeamIdAndProblemId(
                xmlTeamProblem.getSystemId(),
                xmlTeamProblem.getTeam().getId(),
                xmlTeamProblem.getProblem().getId());
        if (teamProblem != null) {
            if ("fresh".equalsIgnoreCase(xmlTeamProblem.getStatus())) {
                logger.info("Skip 'fresh' submission {} from team {}", xmlTeamProblem.getSystemId(), xmlTeamProblem.getTeam().getId());
                return;
            }
            if ("done".equalsIgnoreCase(xmlTeamProblem.getStatus()) &&
                    teamProblem.getJudged()) {
                logger.info("Skip 'done' submission {} from team {}", xmlTeamProblem.getSystemId(), xmlTeamProblem.getTeam().getId());
                return;
            }
        }

        try {
            FeedRunStrategy strategy = selectStrategy(contest);
            if (teamProblem == null) {
                teamProblem = new TeamProblem();
            }
            xmlTeamProblem.mergeTo(teamProblem);
            EventFeedSubmission eventFeedSubmission = strategy.executeTeamProblem(teamProblem, contest);

            sendEventFeedNotification(eventFeedSubmission);

            logger.info("Run " + eventFeedSubmission.getTeamSubmission().getSystemId() + " processed for team " + eventFeedSubmission.getTeamSubmission().getTeam().getSystemId());
        } catch (EventFeedException ex) {
            logger.error("Failed to process run " + xmlTeamProblem.getSystemId() + " for team " + xmlTeamProblem.getTeam().getSystemId(), ex);
        }
    }

    @Override
    public void visit(TestcaseXML testcase, Contest contest) {

    }

    @Override
    public void visit(AnalystMessageXML analystMessage, Contest contest) {
        if (StringUtils.isEmpty(analystMessage.getMessage())) {
            // skip, if the message is empty
            return;
        }
        Notification notification = null;
        if (analystMessage.getRunId() != null) { // notification for submission
            TeamProblem teamProblem = eventFeedDao.findTeamProblemBySystemIdAndTeamContest(analystMessage.getRunId(), contest);
            // if team submission exists and message does not exist
            if (teamProblem != null
                    && notificationDao.getAnalystSubmissionMessageCount(teamProblem, analystMessage.getMessage()) == 0) {
                if (teamProblem.getJudged()) {
                    if (teamProblem.getSolved()) {
                        notification = notificationService.createOnTeamSubmissionSuccess(teamProblem, analystMessage.getMessage());
                    } else {
                        notification = notificationService.createOnTeamSubmissionFailed(teamProblem, analystMessage.getMessage());
                    }
                } else {
                    notification = notificationService.createOnTeamSubmissionSubmitted(teamProblem, analystMessage.getMessage());
                }
            }
        } else if (analystMessage.getTeamId() != null) { // notification for a team
            Team team = eventFeedDao.findTeamBySystemIdAndContest(analystMessage.getTeamId(), contest);
            if (team != null
                    && notificationDao.getAnalystTeamMessageCount(team, analystMessage.getMessage()) == 0) {
                notification = notificationService.createTeamAnalyticsMessage(team, analystMessage.getMessage());
            }

        } else { // general analytics notification
            if (notificationDao.getAnalystGeneralMessageCount(contest, analystMessage.getMessage()) == 0) {
                Contest msgContest = eventFeedDao.findOne(Contest.class, contest.getId());
                notification = notificationService.createAnalyticsMessage(msgContest, analystMessage.getMessage());
            }
        }

        if (notification != null) {
            eventFeedDao.saveContestEntity(notification);
            EventFeedMessage eventFeedMessage = new EventFeedMessage(notification);
            sendEventFeedNotification(eventFeedMessage);
        }
    }

    @Override
    public void visit(FinalizedXML finalizedXML, Contest contest) {

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

    protected <T extends EventFeedEvent> void sendEventFeedNotification(final T event) {
        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(eventFeedQueue);
            connection.start();

            ObjectMessage notificationMessage = session.createObjectMessage(event);
            producer.send(notificationMessage);
        } catch (JMSException e) {
            logger.error("Event feeed JMS notification not send.", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }

        // TODO remove log or improve
        logger.info("Event feed send " + event);
    }
}
