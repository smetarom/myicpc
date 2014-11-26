package com.myicpc.service.webSevice;

import com.myicpc.commons.utils.TextUtils;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.settings.GlobalSettingsService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author Roman Smetana
 */
public abstract class AbstractWSService {
    @Autowired
    protected GlobalSettingsService globalSettingsService;

    /**
     * Connects to ICPC WS and get data from url
     *
     * @param url
     *            url to connect
     * @return received data from WS
     * @throws IOException
     */
    public Object connectCM(String url, final Contest contest) throws IOException {
        return connectCM(globalSettingsService.getGlobalSettings().getContestManagementSystemURL(), url, contest);
    }

    /**
     * Connects to ICPC WS and get data from url
     *
     * @param server
     *            ICPC server URL
     * @param url
     *            url to connect
     * @return received data from WS
     * @throws IOException
     */
    public String connectCM(final String server, final String url, final Contest contest) throws IOException {
        HttpGet httpGet = null;
        try {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(new AuthScope(server, 443), new UsernamePasswordCredentials(contest.getWebServiceSettings()
                    .getWsCMToken(), ""));
            HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

            httpGet = new HttpGet("https://" + server + url);

            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            return IOUtils.toString(entity.getContent(), TextUtils.DEFAULT_ENCODING);
        } finally {
            releaseConnection(httpGet);
        }
    }

    /**
     * Release HTTP connection
     * @param httpGet
     */
    public static void releaseConnection(HttpGet httpGet) {
        if (httpGet != null) {
            httpGet.releaseConnection();
        }
    }
}
