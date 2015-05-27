package com.myicpc.master.service.scoreboard;

import com.myicpc.commons.utils.WebServiceUtils;
import com.myicpc.dto.eventFeed.parser.AnalystMessageXML;
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
import com.myicpc.dto.eventFeed.convertor.ProblemConverter;
import com.myicpc.dto.eventFeed.convertor.TeamConverter;
import com.myicpc.master.dao.EventFeedDao;
import com.myicpc.master.exception.EventFeedException;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.contest.ContestSettings;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.AsyncResult;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.util.concurrent.Future;

/**
 * @author Roman Smetana
 */
@Stateless
@TransactionManagement(value= TransactionManagementType.BEAN)
public class EventFeedProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EventFeedProcessor.class);

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/EventFeedQueue")
    private Queue eventFeedQueue;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private EventFeedLocal eventFeedVisitor;

    @Inject
    private EventFeedDao eventFeedDao;

    @Asynchronous
    public Future<Void> runEventFeed(final Contest contest) {
        ContestSettings contestSettings = contest.getContestSettings();
        if (contestSettings != null && !StringUtils.isEmpty(contestSettings.getEventFeedURL())) {
            try (InputStream in = connectToCDS(contestSettings);
                 Reader reader = new InputStreamReader(in)) {
                XStream xStream = createEventFeedParser(contest);
                ObjectInputStream objectInputStream = xStream.createObjectInputStream(reader);
                final UserTransaction userTransaction = sessionContext.getUserTransaction();
                try {
                    while (true) {
                        if (sessionContext.wasCancelCalled()) {
                            logger.info("Event feed was cancelled.");
                            return new AsyncResult<>(null);
                        }
                        try {
                            XMLEntity elem = (XMLEntity) objectInputStream.readObject();
                            if (!(elem instanceof TestcaseXML)) {
                                elem.setContestId(contest.getId());
                                try {
                                    userTransaction.begin();
                                    elem.accept(eventFeedVisitor, contest);
                                    userTransaction.commit();
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                    try {
                                        userTransaction.rollback();
                                    } catch (SystemException e1) {
                                        // ignore, transaction failed
                                    }
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            logger.warn("XML parsing class not found", e);
                        }
                    }
                } catch (EOFException ex) {
                    logger.info("Event feed parsing is done.");
                }
            } catch (IOException e) {
                logger.error("Communication error with Event feed provider.", e);
            } catch (EventFeedException e) {
                logger.error("Event feed connection failed. Reason: " + e.getMessage());
            }
        } else {
            logger.error("Event feed settings is not correct for contest " + contest.getName());
        }
        return new AsyncResult<>(null);
    }

    private XStream createEventFeedParser(final Contest contest) {
        XStream xStream = new XStream();
        xStream.ignoreUnknownElements();
        xStream.processAnnotations(new Class[]{ContestXML.class, LanguageXML.class, RegionXML.class, JudgementXML.class, ProblemXML.class, TeamXML.class,
                TeamProblemXML.class, TestcaseXML.class, FinalizedXML.class, AnalystMessageXML.class, ClarificationXML.class});
        xStream.registerLocalConverter(TeamProblemXML.class, "problem", new ProblemConverter(contest) {
            @Override
            public Object fromString(String value) {
                return eventFeedDao.getProblemBySystemId(value, contest.getId());
            }
        });
        xStream.registerLocalConverter(TeamProblemXML.class, "team", new TeamConverter(contest) {
            @Override
            public Object fromString(String value) {
                return eventFeedDao.getTeamBySystemId(value, contest.getId());
            }
        });
        return xStream;
    }

    /**
     * Connect to CDS server
     *
     * @return stream with response
     * @throws java.io.IOException
     */
    protected InputStream connectToCDS(final ContestSettings contestSettings) throws IOException, EventFeedException {
        String url = contestSettings.getEventFeedURL();
        String username = contestSettings.getEventFeedUsername();
        String password = contestSettings.getEventFeedPassword();

        return WebServiceUtils.connectCDS(url, username, password);
    }

    protected <T extends XMLEntity> void sendEventFeedNotification(final T event) {
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
