package com.myicpc.service.webSevice;

import com.myicpc.commons.utils.FormatUtils;
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
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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
            SSLContext sslcontext = SSLContexts.custom().build();
            // Allow TLSv1.2 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.2" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(contest.getWebServiceSettings()
                    .getWsCMToken(), ""));
            HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).setSSLSocketFactory(sslsf).build();

            httpGet = new HttpGet("https://" + server + url);

            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            return IOUtils.toString(entity.getContent(), FormatUtils.DEFAULT_ENCODING);
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            throw new IOException(ex);
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
