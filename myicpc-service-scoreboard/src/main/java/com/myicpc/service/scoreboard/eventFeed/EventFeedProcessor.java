package com.myicpc.service.scoreboard.eventFeed;

import com.google.common.io.CountingInputStream;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.commons.utils.WebServiceUtils;
import com.myicpc.dto.eventFeed.parser.AnalystMessageXML;
import com.myicpc.dto.eventFeed.parser.ClarificationXML;
import com.myicpc.dto.eventFeed.parser.ContestXML;
import com.myicpc.dto.eventFeed.parser.EventFeedSettingsDTO;
import com.myicpc.dto.eventFeed.parser.FinalizedXML;
import com.myicpc.dto.eventFeed.parser.JudgementXML;
import com.myicpc.dto.eventFeed.parser.LanguageXML;
import com.myicpc.dto.eventFeed.parser.ProblemXML;
import com.myicpc.dto.eventFeed.parser.RegionXML;
import com.myicpc.dto.eventFeed.parser.ResetXML;
import com.myicpc.dto.eventFeed.parser.TeamProblemXML;
import com.myicpc.dto.eventFeed.parser.TeamXML;
import com.myicpc.dto.eventFeed.parser.TestcaseXML;
import com.myicpc.dto.eventFeed.parser.XMLEntity;
import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor;
import com.myicpc.model.ErrorMessage;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.contest.ContestSettings;
import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.service.notification.ErrorMessageService;
import com.myicpc.service.scoreboard.exception.EventFeedException;
import com.myicpc.service.scoreboard.exception.EventFeedInterrupted;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.util.Date;
import java.util.concurrent.Future;

/**
 * Service for processing the CDS Event feed
 *
 * @author Roman Smetana
 */
@Service
public class EventFeedProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedProcessor.class);
    private static final String EVENT_FEED_OPENING_TAG = "<contest>";
    private static final String EVENT_FEED_ENDING_TAG = "</contest>";
    /**
     * Safe time buffer (in seconds), when the contest stops pulling the source
     */
    private static final int SAFE_TIME_BUFFER = 2 * 60 * 60;
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

    @Autowired
    private ErrorMessageService errorMessageService;

    /**
     * Starts event feed polling for changes
     * <p/>
     * In polls regularly the Event feed in {@code pollPeriod} intervals. The responded event feed
     * is a current snapshot, which is increasingly updated between poll runs.
     * <p/>
     * On poll run, it skips all already seen data and processes only the new part of data.
     *
     * @param contest    contest
     * @param pollPeriod pause interval between poll turns (in milliseconds)
     * @param eventFeedHolder event feed holder
     * @return link to running job
     */
    @Async
    public Future<Void> pollingEventFeed(final Contest contest, long pollPeriod, ControlFeedService.EventFeedHolder eventFeedHolder) {
        ContestSettings contestSettings = contest.getContestSettings();
        if (contestSettings != null && StringUtils.isNotEmpty(contestSettings.getEventFeedURL())) {
            Reader reader = null;
            InputStream in = null;
            logger.info("Starting event feed polling for contest " + contest.getCode());
            long startTime;
            long spendTime;
            while (true) {
                if (Thread.interrupted()) {
                    logger.info("Event feed reader for contest " + contest.getCode() + " was interrupted.");
                    stopEventFeedExecution();
                    break;
                }
                startTime = System.currentTimeMillis();
                try {
                    in = WebServiceUtils.connectCDS(contestSettings.getEventFeedURL(), contestSettings.getEventFeedUsername(),
                            contestSettings.getEventFeedPassword());
                    if (in == null) {
                        // skip this poll turn
                        continue;
                    }
                    reader = new InputStreamReader(in);
                    parseXML(reader, contest);
                    // checks if the contest end date passed
                    if (isContestOver(contest)) {
                        break;
                    }
                } catch (EventFeedInterrupted ex) {
                    // stop event feed processing
                    stopEventFeedExecution();
                    break;
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                    // cannot connect to event feed
                    errorMessageService.createErrorMessage(ErrorMessage.ErrorMessageCause.EVENT_FEED_CONNECTION_FAILED, contest, ex.getMessage());
                } catch (Exception e) {
                    if (eventFeedHolder.isStoppedManually()) {
                        stopEventFeedExecution();
                        break;
                    } else {
                        logger.error(e.getMessage(), e);
                    }
                } finally {
                    IOUtils.closeQuietly(reader);
                    IOUtils.closeQuietly(in);
                    in = null;

                    // wait till the next poll turn
                    spendTime = System.currentTimeMillis() - startTime;
                    if (pollPeriod - spendTime > 0) {
                        try {
                            Thread.sleep(pollPeriod - spendTime);
                        } catch (InterruptedException e1) {
                            // do nothing
                        }
                    }
                }
            }
        } else {
            logger.error("Event feed settings is not correct for contest " + contest.getName());
        }
        logger.info("Event feed polling for contest {} has stopped", contest.getCode());
        return new AsyncResult<>(null);
    }

    @Async
    public Future<Void> pollingEventFeedExperimental(final Contest contest, long pollPeriod) {
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
                    InputStream sourceStream = WebServiceUtils.connectCDS(contestSettings.getEventFeedURL(), contestSettings.getEventFeedUsername(),
                            contestSettings.getEventFeedPassword());
                    if (sourceStream == null) {
                        // skip this poll turn
                        continue;
                    }
                    IOUtils.skipFully(sourceStream, bytesToSkip);
                    String response = IOUtils.toString(sourceStream, FormatUtils.DEFAULT_ENCODING);
                    String endTag = null;
                    if (StringUtils.isNotEmpty(response)) {
                        int indexOfContestEnd = response.lastIndexOf(EVENT_FEED_ENDING_TAG) - 1;
                        if (indexOfContestEnd != -1) {
                            int lastIndexOf = response.lastIndexOf('>', indexOfContestEnd);
                            if (lastIndexOf != -1) {
                                endTag = response.substring(lastIndexOf + 1);
                                response = response.substring(0, lastIndexOf + 1);
                                in = new CountingInputStream(
                                        new ByteArrayInputStream(response.getBytes(FormatUtils.DEFAULT_ENCODING)));
                            }
                        }
                    }
                    if (in != null) {
                        InputStream endingStream = new ByteArrayInputStream(endTag.getBytes(FormatUtils.DEFAULT_ENCODING));
                        if (bytesToSkip == 0) {
                            reader = new InputStreamReader(new SequenceInputStream(in, endingStream));
                        } else {
                            // append starting tag <contest> to make valid XML for parsing
                            InputStream staringStream = new ByteArrayInputStream(EVENT_FEED_OPENING_TAG.getBytes(FormatUtils.DEFAULT_ENCODING));
                            reader = new InputStreamReader(new SequenceInputStream(staringStream, new SequenceInputStream(in, endingStream)));
                        }
                        parseXML(reader, contest);
                    }
                    // checks if the contest end date passed
                    if (isContestOver(contest)) {
                        break;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    if (in != null) {
                        bytesToSkip = bytesToSkip + in.getCount();
                    }
                    IOUtils.closeQuietly(reader);
                    IOUtils.closeQuietly(in);
                    in = null;

                    // wait till the next poll turn
                    spendTime = System.currentTimeMillis() - startTime;
                    if (pollPeriod - spendTime > 0) {
                        try {
                            Thread.sleep(pollPeriod - spendTime);
                        } catch (InterruptedException e1) {
                            // do nothing
                        }
                    }
                }
            }
        } else {
            logger.error("Event feed settings is not correct for contest " + contest.getName());
        }
        logger.info("Event feed polling for contest {} has stopped", contest.getCode());
        return new AsyncResult<>(null);
    }

    private boolean isContestOver(final Contest contest) {
        Contest persistedContest = contestRepository.findOne(contest.getId());
        if (persistedContest.getStartTime() != null) {
            int timeModifier = SAFE_TIME_BUFFER;
            if (persistedContest.getTimeDifference() != null) {
                timeModifier += persistedContest.getTimeDifference() * 60;
            }
            Date endDate = DateUtils.addSeconds(persistedContest.getStartTime(), persistedContest.getLength() + timeModifier);
            return new Date().after(endDate);
        }
        return true;
    }

    /**
     * Starts the streaming event feed processing
     * <p/>
     * It connects to the stream with event feed updates and processes
     * incoming updates. It terminates if the stream is closed.
     * <p/>
     * The processing supports exponential backoff on error during event feed
     * processing.
     *
     * @param contest contest
     * @param holder event feed holder
     * @return link to running job
     */
    @Async
    public Future<Void> runEventFeed(final Contest contest, ControlFeedService.EventFeedHolder holder) {
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
                    if (in == null) {
                        throw new EventFeedException("Event feed stream must not be null.");
                    }
                    reader = new InputStreamReader(in);
                    // reset timer back after successful connection to event feed provider
                    exponentialBackoff = BASE_EXPONENTIAL_BACKOFF;
                    parseXML(reader, contest);
                    break;
                } catch (EventFeedInterrupted ex) {
                    // stop event feed processing
                    stopEventFeedExecution();
                    break;
                } catch (Exception ex) {
                    if (holder.isStoppedManually()) {
                        stopEventFeedExecution();
                        break;
                    } else {
                        logger.error(ex.getMessage(), ex);
                        logger.info("Resuming event feed listener for contest " + contest.getCode() + " after interruption.");
                        exponentialBackoff *= 2;
                        if (exponentialBackoff > MAX_EXPONENTIAL_BACKOFF) {
                            exponentialBackoff = MAX_EXPONENTIAL_BACKOFF;
                        }
                        try {
                            Thread.sleep(exponentialBackoff);
                        } catch (InterruptedException e) {
                            // do nothing
                        }
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

    public void uploadEventFeed(InputStream eventFeedFile, Contest contest) throws EventFeedException {
        Reader reader = null;
        try {
            reader = new InputStreamReader(eventFeedFile);
            EventFeedSettingsDTO eventFeedSettings = new EventFeedSettingsDTO();
            eventFeedSettings.setSkipMessageGeneration(true);
            parseXML(reader, contest, eventFeedSettings);
        } catch (IOException | EventFeedInterrupted e) {
            throw new EventFeedException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private void parseXML(final Reader reader, final Contest contest) throws IOException, EventFeedInterrupted {
        EventFeedSettingsDTO eventFeedSettings = new EventFeedSettingsDTO();
        parseXML(reader, contest, eventFeedSettings);
    }

    private void parseXML(final Reader reader, final Contest contest, final EventFeedSettingsDTO eventFeedSettings) throws IOException, EventFeedInterrupted {
        final XStream xStream = createEventFeedParser();
        int submissionOrder = 0;
        ObjectInputStream in = xStream.createObjectInputStream(reader);
        try {
            while (true) {
                if (Thread.interrupted()) {
                    logger.info("Event feed reader for contest " + contest.getCode() + " was interrupted.");
                    throw new EventFeedInterrupted("Event feed reader for contest " + contest.getCode() + " was interrupted.");
                }
                try {
                    XMLEntity elem = (XMLEntity) in.readObject();
                    if (elem instanceof TeamProblemXML) {
                        ((TeamProblemXML) elem).setSubmissionOrder(submissionOrder++);
                    }
                    elem.accept(eventFeedVisitor, contest, eventFeedSettings);
                } catch (ClassNotFoundException | CannotResolveClassException e) {
                    logger.warn("Non existing Java representation of the XML structure: " + e.getMessage());
                }
            }
        } catch (EOFException ex) {
            logger.info("Event feed parsing is done.");
        }
    }

    private XStream createEventFeedParser() {
        XStream xStream = new XStream();
        xStream.ignoreUnknownElements();
        xStream.processAnnotations(new Class[]{ContestXML.class, LanguageXML.class, RegionXML.class,
                JudgementXML.class, ProblemXML.class, TeamXML.class, TeamProblemXML.class,
                TestcaseXML.class, FinalizedXML.class, ClarificationXML.class,
                ResetXML.class, AnalystMessageXML.class});
        return xStream;
    }

    private void stopEventFeedExecution() {
        Thread.currentThread().stop();
    }
}
