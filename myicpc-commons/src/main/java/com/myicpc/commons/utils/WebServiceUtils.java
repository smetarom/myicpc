package com.myicpc.commons.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Web service util class
 *
 * @author Roman Smetana
 */
public class WebServiceUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebServiceUtils.class);

    /**
     * Connect to CDS server
     * <p>
     * It disables SSL checks to be able to connect to CDS, which uses self-signed certificate
     * with a different host in certificate from the connected host
     *
     * @param url      url to connect
     * @param username username for BASIC authentication
     * @param password password for BASIC authentication
     * @return string response
     * @throws IOException
     */
    public static InputStream connectCDS(final String url, final String username, final String password) throws IOException {
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
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            clientBuilder.setSSLSocketFactory(sslsf);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error("Error during disabling CDS SSL checks", e);
        }

        // TODO http client is not closed!
        CloseableHttpClient client = clientBuilder.build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            throw new IOException(statusLine.getReasonPhrase());
        }
        HttpEntity entity = response.getEntity();
        return entity.getContent();
    }

    /**
     * Connects to CDS and returns the response as text
     *
     * @param url CDS URL
     * @param username CDS username
     * @param password CDS password for username
     * @return server text response or {@code null} if the server did not respond correctly
     */
    public static String connectAndGetResponse(String url, String username, String password) {
        try {
            InputStream inputStream = connectCDS(url, username, password);
            return IOUtils.toString(inputStream, FormatUtils.DEFAULT_ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Releases the open connection from HTTP connection
     *
     * @param httpRequest opened HTTP request
     */
    public static void releaseConnection(HttpRequestBase httpRequest) {
        if (httpRequest != null) {
            httpRequest.releaseConnection();
        }
    }
}
