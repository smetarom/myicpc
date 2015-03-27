package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.enums.FeedRunStrategyType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.contest.ContestSettings;
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.eventFeed.EventFeedControlRepository;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.RegionRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.service.scoreboard.eventFeed.dto.ClarificationXML;
import com.myicpc.service.scoreboard.eventFeed.dto.ContestXML;
import com.myicpc.service.scoreboard.eventFeed.dto.FinalizedXML;
import com.myicpc.service.scoreboard.eventFeed.dto.JudgementXML;
import com.myicpc.service.scoreboard.eventFeed.dto.LanguageXML;
import com.myicpc.service.scoreboard.eventFeed.dto.ProblemXML;
import com.myicpc.service.scoreboard.eventFeed.dto.RegionXML;
import com.myicpc.service.scoreboard.eventFeed.dto.TeamProblemXML;
import com.myicpc.service.scoreboard.eventFeed.dto.TeamXML;
import com.myicpc.service.scoreboard.eventFeed.dto.TestcaseXML;
import com.myicpc.service.scoreboard.eventFeed.dto.XMLEntity;
import com.myicpc.service.scoreboard.eventFeed.dto.convertor.ProblemConverter;
import com.myicpc.service.scoreboard.eventFeed.dto.convertor.TeamConverter;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
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
                TeamProblemXML.class, TestcaseXML.class, FinalizedXML.class, ClarificationXML.class});
        xStream.registerLocalConverter(TeamProblemXML.class, "problem", new ProblemConverter(problemRepository, contest));
        xStream.registerLocalConverter(TeamProblemXML.class, "team", new TeamConverter(teamRepository, contest));
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
