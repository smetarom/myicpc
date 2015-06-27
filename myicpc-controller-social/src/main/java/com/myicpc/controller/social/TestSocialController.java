package com.myicpc.controller.social;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.social.service.InstagramService;
import com.myicpc.social.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.json.DataObjectFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

/**
 * @author Roman Smetana
 */
@Controller
// TODO delete before going to production
public class TestSocialController extends GeneralController {

    @Autowired
    private InstagramService instagramService;

    @Autowired
    private TwitterService twitterService;

    @RequestMapping(value = "/{contestCode}/instagram/subscribe", method = RequestMethod.GET)
    public String instagramSub(@PathVariable final String contestCode) throws IOException, URISyntaxException {
        Contest contest = getContest(contestCode, null);
        String url = "http://localhost:8080/myicpc/CTU-Open-2013/gallery";
        instagramService.startSubscription(contest, url);
        return null;
    }


    @RequestMapping(value = "/{contestCode}/twitter/start", method = RequestMethod.GET)
    public String editContest(@PathVariable final String contestCode) {
        Contest contest = getContest(contestCode, null);

        twitterService.startTwitterStreaming(contest);

        return "redirect:"+getContestURL(contestCode);
    }

    @RequestMapping(value = "/{contestCode}/twitter/send", method = RequestMethod.GET)
    public String sendTweet(@PathVariable final String contestCode) throws TwitterException {
        Contest contest = getContest(contestCode, null);
        Random rand = new Random();

        String media = "\"media\": [{\n" +
                "      \"id\": 266031293949698048,\n" +
                "      \"id_str\": \"266031293949698048\",\n" +
                "      \"indices\": [17, 37],\n" +
                "      \"media_url\": \"http:\\/\\/pbs.twimg.com\\/media\\/A7EiDWcCYAAZT1D.jpg\",\n" +
                "      \"media_url_https\": \"https:\\/\\/pbs.twimg.com\\/media\\/A7EiDWcCYAAZT1D.jpg\",\n" +
                "      \"url\": \"http:\\/\\/t.co\\/bAJE6Vom\",\n" +
                "      \"display_url\": \"pic.twitter.com\\/bAJE6Vom\",\n" +
                "      \"expanded_url\": \"http:\\/\\/twitter.com\\/BarackObama\\/status\\/266031293945503744\\/photo\\/1\",\n" +
                "      \"type\": \"photo\",\n" +
                "      \"sizes\": {\n" +
                "        \"medium\": {\n" +
                "          \"w\": 600,\n" +
                "          \"h\": 399,\n" +
                "          \"resize\": \"fit\"\n" +
                "        },\n" +
                "        \"thumb\": {\n" +
                "          \"w\": 150,\n" +
                "          \"h\": 150,\n" +
                "          \"resize\": \"crop\"\n" +
                "        },\n" +
                "        \"small\": {\n" +
                "          \"w\": 340,\n" +
                "          \"h\": 226,\n" +
                "          \"resize\": \"fit\"\n" +
                "        },\n" +
                "        \"large\": {\n" +
                "          \"w\": 800,\n" +
                "          \"h\": 532,\n" +
                "          \"resize\": \"fit\"\n" +
                "        }\n" +
                "      }\n" +
                "    }]";
        String s = "{\n" +
                "\"coordinates\":null,\n" +
                "\"favorited\":false,\n" +
                "\"truncated\":false,\n" +
                "\"created_at\":\"Mon Sep 24 03:35:21 +0000 2012\",\n" +
                "\"id_str\":\"250075927172759552\",\n" +
                "\"entities\":{\n" +
                media + ",\n" +
                "\"urls\":[\n" +
                "],\n" +
                "\"hashtags\":[\n" +
                "{\n" +
                "\"text\":\"freebandnames\",\n" +
                "\"indices\":[\n" +
                "20,\n" +
                "34\n" +
                "]\n" +
                "}\n" +
                "],\n" +
                "\"user_mentions\":[\n" +
                "]\n" +
                "},\n" +
                "\"in_reply_to_user_id_str\":null,\n" +
                "\"contributors\":null,\n" +
                "\"text\":\"Aggressive Ponytail #freebandnames\",\n" +
                "\"metadata\":{\n" +
                "\"iso_language_code\":\"en\",\n" +
                "\"result_type\":\"recent\"\n" +
                "},\n" +
                "\"retweet_count\":0,\n" +
                "\"in_reply_to_status_id_str\":null,\n" +
                "\"id\":"+rand.nextLong()+",\n" +
                "\"geo\":null,\n" +
                "\"retweeted\":false,\n" +
                "\"in_reply_to_user_id\":null,\n" +
                "\"place\":null,\n" +
                "\"user\":{\n" +
                "\"profile_sidebar_fill_color\":\"DDEEF6\",\n" +
                "\"profile_sidebar_border_color\":\"C0DEED\",\n" +
                "\"profile_background_tile\":false,\n" +
                "\"name\":\"Sean Cummings\",\n" +
                "\"profile_image_url\":\"http://abs.twimg.com/sticky/default_profile_images/default_profile_3_normal.png\",\n" +
                "\"created_at\":\"Mon Apr 26 06:01:55 +0000 2010\",\n" +
                "\"location\":\"LA, CA\",\n" +
                "\"follow_request_sent\":null,\n" +
                "\"profile_link_color\":\"0084B4\",\n" +
                "\"is_translator\":false,\n" +
                "\"id_str\":\"137238150\",\n" +
                "\"entities\":{\n" +
                "\"url\":{\n" +
                "\"urls\":[\n" +
                "{\n" +
                "\"expanded_url\":null,\n" +
                "\"url\":\"\",\n" +
                "\"indices\":[\n" +
                "0,\n" +
                "0\n" +
                "]\n" +
                "}\n" +
                "]\n" +
                "},\n" +
                "\"description\":{\n" +
                "\"urls\":[\n" +
                "]\n" +
                "}\n" +
                "},\n" +
                "\"default_profile\":true,\n" +
                "\"contributors_enabled\":false,\n" +
                "\"favourites_count\":0,\n" +
                "\"url\":null,\n" +
                "\"profile_image_url_https\":\"https://si0.twimg.com/profile_images/2359746665/1v6zfgqo8g0d3mk7ii5s_normal.jpeg\",\n" +
                "\"utc_offset\":-28800,\n" +
                "\"id\":137238150,\n" +
                "\"profile_use_background_image\":true,\n" +
                "\"listed_count\":2,\n" +
                "\"profile_text_color\":\"333333\",\n" +
                "\"lang\":\"en\",\n" +
                "\"followers_count\":70,\n" +
                "\"protected\":false,\n" +
                "\"notifications\":null,\n" +
                "\"profile_background_image_url_https\":\"https://si0.twimg.com/images/themes/theme1/bg.png\",\n" +
                "\"profile_background_color\":\"C0DEED\",\n" +
                "\"verified\":false,\n" +
                "\"geo_enabled\":true,\n" +
                "\"time_zone\":\"Pacific Time (US & Canada)\",\n" +
                "\"description\":\"Born 330 Live 310\",\n" +
                "\"default_profile_image\":false,\n" +
                "\"profile_background_image_url\":\"http://a0.twimg.com/images/themes/theme1/bg.png\",\n" +
                "\"statuses_count\":579,\n" +
                "\"friends_count\":110,\n" +
                "\"following\":null,\n" +
                "\"show_all_inline_media\":false,\n" +
                "\"screen_name\":\"sean_cummings\"\n" +
                "},\n" +
                "\"in_reply_to_screen_name\":null,\n" +
                "\"source\":\"<a>Twitter for Mac</a>\",\n" +
                "\"in_reply_to_status_id\":null\n" +
                "}";
        Status status = DataObjectFactory.createStatus(s);


//        twitterService.createTwitterNotificationFromStatus(status, contest);

        return null;
    }

}
