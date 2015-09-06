package com.myicpc.controller.social;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.controller.GeneralController;
import com.myicpc.social.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Roman Smetana
 */
@Controller
public class SocialCheckController extends GeneralController {
    @Autowired
    private TwitterService twitterService;

    @RequestMapping(value = "/check/twitter", method = RequestMethod.POST)
    @ResponseBody
    public String checkTwitterConfiguration(@RequestParam String twitterConsumerKey,
            @RequestParam String twitterConsumerSecret,
            @RequestParam String twitterAccessToken,
            @RequestParam String twitterAccessTokenSecret) throws IOException, URISyntaxException {

        boolean success = twitterService.checkTwitterConfiguration(twitterConsumerKey, twitterConsumerSecret, twitterAccessToken, twitterAccessTokenSecret);
        String key = success ? "contest.twitter.checkWS.success" : "contest.twitter.checkWS.failed";

        return MessageUtils.getMessage(key);
    }
}
