package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.commons.utils.WebServiceUtils;
import com.myicpc.enums.FeedRunStrategyType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.contest.ContestSettings;
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.eventFeed.*;
import com.myicpc.service.scoreboard.eventFeed.dto.*;
import com.myicpc.service.scoreboard.eventFeed.dto.convertor.ProblemConverter;
import com.myicpc.service.scoreboard.eventFeed.dto.convertor.RegionConverter;
import com.myicpc.service.scoreboard.eventFeed.dto.convertor.TeamConverter;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.concurrent.Future;

@Service
public class EventFeedProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedProcessor.class);

    @Autowired
    private EventFeedWSService eventFeedWSService;

    @Autowired
    private EventFeedVisitor eventFeedVisitor;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private EventFeedControlRepository eventFeedControlRepository;

    @Autowired
    private RegionRepository regionRepository;

    public void run() {
        for (Contest contest : contestRepository.findAll()) {
            runEventFeed(contest);
        }
    }

    public void initContest() {
        Contest contest = new Contest();
        ContestSettings contestSettings = new ContestSettings();
        contestSettings.setEventFeedURL("http://myicpc.baylor.edu/simulator/2015/events");
        contestSettings.setEventFeedUsername("myicpc");
        contestSettings.setEventFeedPassword("br1sket");
        contestSettings.setScoreboardStrategyType(FeedRunStrategyType.NATIVE);
        contestSettings.setGenerateMessages(true);
        contest.setName("2014 World Finals");
        contest.setCode("WF-2014");
        contest.setExternalId(1811L);
        contest.setContestSettings(contestSettings);
        contestRepository.save(contest);
    }

    @Async
    public Future<Void> runEventFeed(final Contest contest) {
        ContestSettings contestSettings = contest.getContestSettings();
        if (contestSettings != null && !StringUtils.isEmpty(contestSettings.getEventFeedURL())) {
            EventFeedControl eventFeedControl = getCurrentEventFeedControl(contest);
            Reader reader = null;
            InputStream in = null;
            try {
                in = eventFeedWSService.connectCDS(contestSettings.getEventFeedURL(), contestSettings.getEventFeedUsername(),
                        contestSettings.getEventFeedPassword());
                reader = new InputStreamReader(in);
                parseXML(reader, contest, eventFeedControl);
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            } catch (EventFeedException ex) {
                logger.error("Event feed connection failed. Reason: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                logger.error("Non existing Java representation of the XML sctructure", ex);
            } catch (Exception ex) {
                logger.error("Unexpected error occured", ex);
            } finally {
                IOUtils.closeQuietly(reader);
                IOUtils.closeQuietly(in);
            }
        } else {
            logger.error("Event feed settings is not correct for contest " + contest.getName());
        }
        return new AsyncResult<Void>(null);
    }

    protected void parseXML(final Reader reader, final Contest contest, EventFeedControl eventFeedControl) throws IOException, ClassNotFoundException {
        XStream xStream = new XStream();
        xStream.ignoreUnknownElements();
        xStream.processAnnotations(new Class[]{ContestXML.class, LanguageXML.class, RegionXML.class, JudgementXML.class, ProblemXML.class, TeamXML.class,
                TeamProblemXML.class, TestcaseXML.class, FinalizedXML.class});
        xStream.registerLocalConverter(TeamProblemXML.class, "problem", new ProblemConverter(problemRepository, contest));
        xStream.registerLocalConverter(TeamProblemXML.class, "team", new TeamConverter(teamRepository, contest));
        xStream.registerLocalConverter(TeamXML.class, "region", new RegionConverter(regionRepository, contest));
        ObjectInputStream in = xStream.createObjectInputStream(reader);
        try {
            while (true) {
                if (Thread.interrupted()) {
                    logger.info("Event feed reader for contest " + contest.getCode() + " was interrupted.");
                    break;
                }
                XMLEntity elem = (XMLEntity) in.readObject();
                elem.accept(eventFeedVisitor, contest, eventFeedControl);
            }
        } catch (EOFException ex) {
            logger.info("Event feed parsing is done.");
        }
    }

    @Transactional
    private EventFeedControl getCurrentEventFeedControl(Contest contest) {
        EventFeedControl eventFeedControl = eventFeedControlRepository.findByContest(contest);
        if (eventFeedControl == null) {
            eventFeedControl = new EventFeedControl(contest);
            eventFeedControl = eventFeedControlRepository.save(eventFeedControl);
        }
        eventFeedControl.restartControl();
        return eventFeedControl;
    }
}
