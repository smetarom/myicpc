package com.myicpc.service.scoreboard.eventFeed;

import com.google.common.io.CountingInputStream;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.commons.utils.WebServiceUtils;
import com.myicpc.dto.eventFeed.convertor.ProblemConverter;
import com.myicpc.dto.eventFeed.convertor.TeamConverter;
import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.contest.ContestSettings;
import com.myicpc.model.eventFeed.EventFeedControl;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.eventFeed.EventFeedControlRepository;
import com.myicpc.repository.eventFeed.ProblemRepository;
import com.myicpc.repository.eventFeed.RegionRepository;
import com.myicpc.repository.eventFeed.TeamRepository;
import com.myicpc.dto.eventFeed.parser.ClarificationXML;
import com.myicpc.dto.eventFeed.parser.ContestXML;
import com.myicpc.dto.eventFeed.parser.FinalizedXML;
import com.myicpc.dto.eventFeed.parser.JudgementXML;
import com.myicpc.dto.eventFeed.parser.LanguageXML;
import com.myicpc.dto.eventFeed.parser.ProblemXML;
import com.myicpc.dto.eventFeed.parser.RegionXML;
import com.myicpc.dto.eventFeed.parser.TeamProblemXML;
import com.myicpc.dto.eventFeed.parser.TeamXML;
import com.myicpc.dto.eventFeed.parser.TestcaseXML;
import com.myicpc.dto.eventFeed.parser.XMLEntity;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.util.concurrent.Future;

@Service
public class EventFeedProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedProcessor.class);
    private static final String EVENT_FEED_OPENING_TAG = "<contest>";
    /**
     * Default value of the backoff timer in ms
     */
    private static final long BASE_EXPONENTIAL_BACKOFF = 1000;
    /**
     * Maximum value of the backoff timer in ms
     */
    private static final long MAX_EXPONENTIAL_BACKOFF = 60 * BASE_EXPONENTIAL_BACKOFF;

    @Autowired
    private EventFeedVisitor eventFeedVisitor;

    @Autowired
    private ContestRepository contestRepository;

    public void run() {
        for (Contest contest : contestRepository.findAll()) {
            runEventFeed(contest);
        }
    }

    @Async
    public Future<Void> pollingEventFeed(final Contest contest, long pollPeriod) {
        ContestSettings contestSettings = contest.getContestSettings();
        if (contestSettings != null && !StringUtils.isEmpty(contestSettings.getEventFeedURL())) {
            Reader reader = null;
            CountingInputStream in = null;
            logger.info("Starting event feed polling for contest " + contest.getCode());
            long startTime;
            long spendTime;
            long bytesToSkip = 0;
            while (true) {
                startTime = System.currentTimeMillis();
                try {
                    in = new CountingInputStream(WebServiceUtils.connectCDS(contestSettings.getEventFeedURL(), contestSettings.getEventFeedUsername(),
                            contestSettings.getEventFeedPassword()));
                    IOUtils.skipFully(in, bytesToSkip);
                    if (bytesToSkip == 0) {
                        reader = new InputStreamReader(in);
                    } else {
                        // append starting tag <contest> to make valid XML for parsing
                        InputStream staringStream = new ByteArrayInputStream(EVENT_FEED_OPENING_TAG.getBytes(FormatUtils.DEFAULT_ENCODING));
                        reader = new InputStreamReader(new SequenceInputStream(staringStream, in));
                    }
                    parseXML(reader, contest);
                    break;
                } catch (StreamException e) {
                    // invalid XML, expected during the polling
                    spendTime = System.currentTimeMillis() - startTime;
                    if (pollPeriod - spendTime > 0) {
                        try {
                            Thread.sleep(pollPeriod - spendTime);
                        } catch (InterruptedException e1) {
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    if (in != null) {
                        bytesToSkip = in.getCount();
                    }
                    IOUtils.closeQuietly(reader);
                    IOUtils.closeQuietly(in);
                }
            }
        } else {
            logger.error("Event feed settings is not correct for contest " + contest.getName());
        }
        logger.info("Event feed polling for contest {} has stopped", contest.getCode());
        return new AsyncResult<>(null);
    }

    @Async
    public Future<Void> runEventFeed(final Contest contest) {
        ContestSettings contestSettings = contest.getContestSettings();
        if (contestSettings != null && !StringUtils.isEmpty(contestSettings.getEventFeedURL())) {
            Reader reader = null;
            InputStream in = null;
            long exponentialBackoff = BASE_EXPONENTIAL_BACKOFF;
            logger.info("Starting event feed listener for contest " + contest.getCode());
            while (true) {
                try {
                    in = WebServiceUtils.connectCDS(contestSettings.getEventFeedURL(), contestSettings.getEventFeedUsername(),
                            contestSettings.getEventFeedPassword());
                    reader = new InputStreamReader(in);
                    // reset timer back after successful connection to event feed provider
                    exponentialBackoff = BASE_EXPONENTIAL_BACKOFF;
                    parseXML(reader, contest);
                    break;
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    logger.info("Resuming event feed listener for contest " + contest.getCode() + " after interruption.");
                    exponentialBackoff *= 2;
                    if (exponentialBackoff > MAX_EXPONENTIAL_BACKOFF) {
                        exponentialBackoff = MAX_EXPONENTIAL_BACKOFF;
                    }
                    try {
                        Thread.sleep(exponentialBackoff);
                    } catch (InterruptedException e) {
                    }
                } finally {
                    IOUtils.closeQuietly(reader);
                    IOUtils.closeQuietly(in);
                }
            }
        } else {
            logger.error("Event feed settings is not correct for contest " + contest.getName());
        }
        logger.info("Event feed listener for contest {} has stopped", contest.getCode());
        return new AsyncResult<>(null);
    }

    protected void parseXML(final Reader reader, final Contest contest) throws IOException {
        final XStream xStream = createEventFeedParser();
        ObjectInputStream in = xStream.createObjectInputStream(reader);
        try {
            while (true) {
                if (Thread.interrupted()) {
                    logger.info("Event feed reader for contest " + contest.getCode() + " was interrupted.");
                    break;
                }
                // Ignore testcases
                try {
                    XMLEntity elem = (XMLEntity) in.readObject();
                    elem.accept(eventFeedVisitor, contest);
                } catch (ClassNotFoundException e) {
                    logger.warn("Non existing Java representation of the XML structure: " + e.getMessage(), e);
                }
            }
        } catch (EOFException ex) {
            logger.info("Event feed parsing is done.");
        }
    }

    private XStream createEventFeedParser() {
        XStream xStream = new XStream();
        xStream.ignoreUnknownElements();
        xStream.processAnnotations(new Class[]{ContestXML.class, LanguageXML.class, RegionXML.class, JudgementXML.class, ProblemXML.class, TeamXML.class,
                TeamProblemXML.class, TestcaseXML.class, FinalizedXML.class, ClarificationXML.class});
        return xStream;
    }
}
