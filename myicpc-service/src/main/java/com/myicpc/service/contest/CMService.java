package com.myicpc.service.contest;

import com.myicpc.commons.utils.TextUtils;
import com.myicpc.model.contest.Contest;
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
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Roman Smetana
 */
@Service
public class CMService {
    private static final String ICPC_URL = "icpc.baylor.edu";

    /**
     * Get JSON data about universities from ICPC WS
     *
     * @return JSON data
     * @throws java.io.IOException
     */
    public String getUniversitiesFromCM(final Contest contest) throws IOException {
        return (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/institutions", contest);
    }

    /**
     * Get JSON data about teams from ICPC WS
     *
     * @return JSON data
     * @throws IOException
     */
    public String getTeamsFromCM(final Contest contest) throws IOException {
        return (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/teams", contest);
    }

    public String getTeamCoordinatesCM(final Contest contest) throws IOException {
        return (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/map-coordinates", contest);
    }

    /**
     * Get JSON data about staff members from ICPC WS
     *
     * @return JSON data
     * @throws IOException
     */
    public String getStaffMembersFromCM(final Contest contest) throws IOException {
        return (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/staff-members", contest);
    }

    /**
     * Get JSON data about social info from ICPC WS
     *
     * @return JSON data
     * @throws IOException
     */
    public String getSocialInfosFromCM(final Contest contest) throws IOException {
        return (String) connectCM("/ws/myicpc/contest/" + contest.getCode() + "/social-info", contest);
    }

    /**
     * Connects to ICPC WS and get data from url
     *
     * @param url url to connect
     * @return received data from WS
     * @throws IOException
     */
    public Object connectCM(String url, final Contest contest) throws IOException {
        return connectCM(ICPC_URL, url, contest);
    }

    /**
     * Connects to ICPC WS and get data from url
     *
     * @param server ICPC server URL
     * @param url    url to connect
     * @return received data from WS
     * @throws IOException
     */
    public String connectCM(final String server, final String url, final Contest contest) throws IOException {
        HttpGet httpGet = null;
        try {
            System.out.println(contest.getWebServiceSettings()
                    .getWsCMToken());
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

    private void releaseConnection(HttpGet httpGet) {
        if (httpGet != null) {
            httpGet.releaseConnection();
        }
    }
}
