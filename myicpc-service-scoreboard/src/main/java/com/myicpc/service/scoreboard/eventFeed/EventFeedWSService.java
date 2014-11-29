package com.myicpc.service.scoreboard.eventFeed;

import com.myicpc.service.scoreboard.exception.EventFeedException;
import com.myicpc.service.webSevice.AbstractWSService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class EventFeedWSService extends AbstractWSService {
    /**
     * Connect to CDS server
     *
     * @param url      url to connect
     * @param username username for BASIC authentication
     * @param password password for BASIC authentication
     * @return stream with response
     * @throws java.io.IOException
     */
    public InputStream connectCDS(final String url, final String username, final String password) throws IOException, EventFeedException {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (!StringUtils.isEmpty(username)) {
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        }
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            throw new EventFeedException(statusLine.getReasonPhrase());
        }
        HttpEntity entity = response.getEntity();
        return entity.getContent();
    }
}
