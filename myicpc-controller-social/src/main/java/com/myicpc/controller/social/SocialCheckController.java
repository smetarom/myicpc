package com.myicpc.controller.social;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.controller.GeneralController;
import com.myicpc.social.service.InstagramService;
import com.myicpc.social.service.TwitterService;
import com.myicpc.social.service.VineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Roman Smetana
 */
@Controller
public class SocialCheckController extends GeneralController {
    @Autowired
    private TwitterService twitterService;

    @Autowired
    private VineService vineService;

    @Autowired
    private InstagramService instagramService;

    @RequestMapping(value = "/social/check/twitter", method = RequestMethod.POST)
    @ResponseBody
    public String checkTwitterConfiguration(@RequestParam String twitterConsumerKey,
            @RequestParam String twitterConsumerSecret,
            @RequestParam String twitterAccessToken,
            @RequestParam String twitterAccessTokenSecret) {
        boolean success;
        try {
            success = twitterService.checkTwitterConfiguration(twitterConsumerKey, twitterConsumerSecret, twitterAccessToken, twitterAccessTokenSecret);
        } catch (Exception e) {
            success = false;
        }
        String key = success ? "contest.twitter.checkWS.success" : "contest.twitter.checkWS.failed";

        return MessageUtils.getMessage(key);
    }

    @RequestMapping(value = "/social/check/instagram", method = RequestMethod.POST)
    @ResponseBody
    public String checkInstagramConfiguration(@RequestParam String instagramClientId) {
        boolean success = instagramService.checkInstagramConfiguration(instagramClientId);
        String key = success ? "contest.instagram.checkWS.success" : "contest.instagram.checkWS.failed";

        return MessageUtils.getMessage(key);
    }

    @RequestMapping(value = "/social/check/vine", method = RequestMethod.POST)
    @ResponseBody
    public String checkVineConfiguration(@RequestParam String vineUsername, @RequestParam String vinePassword) {
        boolean success = vineService.checkVineConfiguration(vineUsername, vinePassword);
        String key = success ? "contest.vine.checkWS.success" : "contest.vine.checkWS.failed";

        return MessageUtils.getMessage(key);
    }
}
