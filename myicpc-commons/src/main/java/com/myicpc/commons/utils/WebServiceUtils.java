package com.myicpc.commons.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Roman Smetana
 */
public class WebServiceUtils {


    /**
     * Connect to CDS server
     *
     * @param url      url to connect
     * @param username username for BASIC authentication
     * @param password password for BASIC authentication
     * @return string response
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static InputStream connectCDS(final String url, final String username, final String password) throws ClientProtocolException, IOException {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        return entity.getContent();
    }

    public static void releaseConnection(HttpGet httpGet) {
        if (httpGet != null) {
            httpGet.releaseConnection();
        }
    }
}
