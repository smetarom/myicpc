package com.myicpc.social;

import com.myicpc.model.contest.Contest;
import com.myicpc.service.exception.WebServiceException;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Service
public class InstagramService extends ASocialService {

    public void startSubscription(final Contest contest, String callbackUrl) throws URISyntaxException, IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            URI uri = new URIBuilder().setScheme("https")
                    .setHost("api.instagram.com")
                    .setPath("/v1/subscriptions/")
                    .build();
            List<NameValuePair> formparams = new ArrayList<>();
            formparams.add(new BasicNameValuePair("client_id", contest.getWebServiceSettings().getInstagramKey()));
            formparams.add(new BasicNameValuePair("client_secret", contest.getWebServiceSettings().getInstagramSecret()));
            formparams.add(new BasicNameValuePair("object", "tag"));
            formparams.add(new BasicNameValuePair("aspect", "media"));
            formparams.add(new BasicNameValuePair("object_id", contest.getHashtag()));
            formparams.add(new BasicNameValuePair("callback_url", callbackUrl));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(entity);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            System.out.println(response.getStatusLine().getStatusCode());
            response.close();
        }
    }

    public void getNewPosts(final Contest contest) throws WebServiceException {
        String json = "{\n" +
                "      \"attribution\": null,\n" +
                "      \"tags\":  [\n" +
                "        \"winter\",\n" +
                "        \"nature\",\n" +
                "        \"naturelovers\",\n" +
                "        \"sun\",\n" +
                "        \"snowflakes\",\n" +
                "        \"snow\",\n" +
                "        \"forest\"\n" +
                "      ],\n" +
                "      \"location\": null,\n" +
                "      \"comments\":  {\n" +
                "        \"count\": 0,\n" +
                "        \"data\":  []\n" +
                "      },\n" +
                "      \"filter\": \"Normal\",\n" +
                "      \"created_time\": \"1424022995\",\n" +
                "      \"link\": \"http://instagram.com/p/zIYjVvK0w0/\",\n" +
                "      \"likes\":  {\n" +
                "        \"count\": 0,\n" +
                "        \"data\":  []\n" +
                "      },\n" +
                "      \"images\":  {\n" +
                "        \"low_resolution\":  {\n" +
                "          \"url\": \"http://scontent-b.cdninstagram.com/hphotos-xpf1/t51.2885-15/s306x306/e15/10525525_778000858943718_1658502479_n.jpg\",\n" +
                "          \"width\": 306,\n" +
                "          \"height\": 306\n" +
                "        },\n" +
                "        \"thumbnail\":  {\n" +
                "          \"url\": \"http://scontent-b.cdninstagram.com/hphotos-xpf1/t51.2885-15/s150x150/e15/10525525_778000858943718_1658502479_n.jpg\",\n" +
                "          \"width\": 150,\n" +
                "          \"height\": 150\n" +
                "        },\n" +
                "        \"standard_resolution\":  {\n" +
                "          \"url\": \"http://scontent-b.cdninstagram.com/hphotos-xpf1/t51.2885-15/e15/10525525_778000858943718_1658502479_n.jpg\",\n" +
                "          \"width\": 640,\n" +
                "          \"height\": 640\n" +
                "        }\n" +
                "      },\n" +
                "      \"users_in_photo\":  [],\n" +
                "      \"caption\":  {\n" +
                "        \"created_time\": \"1424022995\",\n" +
                "        \"text\": \"#snow #snowflakes #winter #forest #nature #naturelovers #sun\",\n" +
                "        \"from\":  {\n" +
                "          \"username\": \"haris_dalagija\",\n" +
                "          \"profile_picture\": \"https://instagramimages-a.akamaihd.net/profiles/profile_506822770_75sq_1376417812.jpg\",\n" +
                "          \"id\": \"506822770\",\n" +
                "          \"full_name\": \"Haris Dalagija\"\n" +
                "        },\n" +
                "        \"id\": \"921094105728765275\"\n" +
                "      },\n" +
                "      \"type\": \"image\",\n" +
                "      \"id\": \"921094105435163700_506822770\",\n" +
                "      \"user\":  {\n" +
                "        \"username\": \"haris_dalagija\",\n" +
                "        \"website\": \"\",\n" +
                "        \"profile_picture\": \"https://instagramimages-a.akamaihd.net/profiles/profile_506822770_75sq_1376417812.jpg\",\n" +
                "        \"full_name\": \"Haris Dalagija\",\n" +
                "        \"bio\": \"\",\n" +
                "        \"id\": \"506822770\"\n" +
                "      }\n" +
                "    }";
        

    }



}
