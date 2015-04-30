package com.myicpc.master.service.scoreboard;

import com.myicpc.dto.eventFeed.ClarificationXML;
import com.myicpc.dto.eventFeed.ContestXML;
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
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
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
import java.util.concurrent.Future;

/**
 * @author Roman Smetana
 */
@Stateless
public class ScoreboardService {
    private static final Logger logger = LoggerFactory.getLogger(ScoreboardService.class);

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/SocialNotificationQueue")
    private Queue socialQueue;

    @Asynchronous
    public Future<Void> runEventFeed(final Contest contest) {
        ContestSettings contestSettings = contest.getContestSettings();
        if (contestSettings != null && !StringUtils.isEmpty(contestSettings.getEventFeedURL())) {
            // TODO load latest event feed control from db

            try (InputStream in = connectToCDS(contestSettings);
                 Reader reader = new InputStreamReader(in)) {
                XStream xStream = createEventFeedParser(contest);
                ObjectInputStream objectInputStream = xStream.createObjectInputStream(reader);
                try {
                    while (true) {
                        if (Thread.interrupted()) {
                            logger.info("Event feed reader for contest " + contest.getCode() + " was interrupted.");
                            break;
                        }
                        try {
                            XMLEntity elem = (XMLEntity) objectInputStream.readObject();
                            sendEventFeedNotification(elem);
                        } catch (ClassNotFoundException e) {
                            logger.warn("XML parsing class not found", e);
                        }
                    }
                } catch (EOFException ex) {
                    logger.info("Event feed parsing is done.");
                }
            } catch (IOException e) {
                e.printStackTrace();
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
                TeamProblemXML.class, TestcaseXML.class, FinalizedXML.class, ClarificationXML.class});
        xStream.registerLocalConverter(TeamProblemXML.class, "problem", new ProblemConverter(contest) {
            @Override
            public Object fromString(String value) {
                return null;
            }
        });
        xStream.registerLocalConverter(TeamProblemXML.class, "team", new TeamConverter(contest) {
            @Override
            public Object fromString(String value) {
                return null;
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
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (!StringUtils.isEmpty(username)) {
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        }

        // Build connection client
        HttpClientBuilder clientBuilder = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider);

        // Turn off host verification in SSL
        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            clientBuilder.setSSLSocketFactory(sslsf);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error("Error during disabling Event feed SSL checks", e);
        }

        // TODO http client is not closed!
        CloseableHttpClient client = clientBuilder.build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            throw new EventFeedException(statusLine.getReasonPhrase());
        }
        HttpEntity entity = response.getEntity();
        return entity.getContent();
    }

    protected <T extends XMLEntity> void sendEventFeedNotification(final T event) {
        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(socialQueue);
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
