package com.myicpc.master.service.scoreboard;

import com.google.common.collect.Maps;
import com.myicpc.dto.eventFeed.ClarificationXML;
import com.myicpc.dto.eventFeed.ContestXML;
import com.myicpc.dto.eventFeed.EventFeedCommand;
import com.myicpc.dto.eventFeed.EventFeedResponse;
import com.myicpc.dto.eventFeed.FinalizedXML;
import com.myicpc.dto.eventFeed.JudgementXML;
import com.myicpc.dto.eventFeed.LanguageXML;
import com.myicpc.dto.eventFeed.ProblemXML;
import com.myicpc.dto.eventFeed.RegionXML;
import com.myicpc.dto.eventFeed.TeamProblemXML;
import com.myicpc.dto.eventFeed.TeamXML;
import com.myicpc.dto.eventFeed.TestcaseXML;
import com.myicpc.dto.eventFeed.XMLEntity;
import com.myicpc.dto.eventFeed.convertor.ProblemConverter;
import com.myicpc.dto.eventFeed.convertor.TeamConverter;
import com.myicpc.master.bean.IMasterBean;
import com.myicpc.master.dao.ContestDao;
import com.myicpc.master.dao.EventFeedDao;
import com.myicpc.master.exception.EventFeedException;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.contest.ContestSettings;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.AsyncResult;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Roman Smetana
 */
@Stateless
public class EventFeedService {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedService.class);
    private static final ConcurrentMap<Long, Future<Void>> runningFeedProcessors = Maps.newConcurrentMap();

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/EventFeedQueue")
    private Queue eventFeedQueue;

    @EJB(beanName = "ScoreboardBean")
    private IMasterBean scoreboardBean;

    @Inject
    private EventFeedDao eventFeedDao;

    @Inject
    private ContestDao contestDao;

    @Inject
    private EventFeedProcessor eventFeedProcessor;

    public void startEventFeed(final Contest contest) {
        Future<Void> running = runningFeedProcessors.get(contest.getId());
//        if (running != null && !running.isDone()) {
//            // TODO logging
//            return;
//        }
        Future<Void> newFeedProcessor = eventFeedProcessor.runEventFeed(contest);
        runningFeedProcessors.put(contest.getId(), newFeedProcessor);
    }

    public void stopEventFeed(Contest contest) {
        Future<Void> running = runningFeedProcessors.get(contest.getId());
        if (running != null && !running.isDone()) {
            boolean cancelled = running.cancel(true);
            if (!cancelled) {
                // TODO logging
                return;
            }
        }
    }

    public synchronized void processReceivedCommand(ObjectMessage rcvMessage, final EventFeedCommand command) {
        System.out.println("Scoreboard bean " + scoreboardBean.getStarted());
        // if this bean is running
        if (scoreboardBean.getStarted().get()) {
            Contest contest = contestDao.getContestById(command.getContestId());
            // stop feed
            stopEventFeed(contest);
            // truncate data if requested
            if (command.isTruncateDatabase()) {
                truncateEventFeedData(contest);
            }
            // start feed if requested
            if (command.isStart()) {
                startEventFeed(contest);
            }

            // response to the command
//            Connection connection = null;
//
//            try {
//                connection = connectionFactory.createConnection();
//                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//                MessageProducer replyProducer = session.createProducer(rcvMessage.getJMSReplyTo());
//                connection.start();
//
//                ObjectMessage response = session.createObjectMessage(new EventFeedResponse(200));
//                response.setJMSCorrelationID(rcvMessage.getJMSCorrelationID());
//                replyProducer.send(response);
//            } catch (JMSException e) {
//                logger.error("Event feed control response not send.", e);
//            } finally {
//                if (connection != null) {
//                    try {
//                        connection.close();
//                    } catch (JMSException e) {
//                    }
//                }
//            }

        }
    }

    protected void truncateEventFeedData(final Contest contest) {
        eventFeedDao.deleteAllEventFeedData(contest);
    }

}
