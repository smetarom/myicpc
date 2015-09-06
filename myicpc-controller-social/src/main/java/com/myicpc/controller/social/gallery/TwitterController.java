package com.myicpc.controller.social.gallery;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.social.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import twitter4j.TwitterException;

/**
 * @author Roman Smetana
 */
@Controller
public class TwitterController extends GeneralController {
    private static final Logger logger = LoggerFactory.getLogger(TwitterController.class);

    @Autowired
    private TwitterService twitterService;

    @RequestMapping(value = "/private/{contestCode}/twitter", method = RequestMethod.GET)
    public String twitterPage(@PathVariable final String contestCode, final Model model) {
        getContest(contestCode, model);

        return "private/twitter/twitter";
    }

    @RequestMapping(value = "/private/{contestCode}/twitter", method = RequestMethod.POST)
    public String processAdditionalTweets(@PathVariable String contestCode,
                                          @RequestParam String hashtags,
                                          @RequestParam(required = false) Integer count,
                                          @RequestParam(required = false) Integer pages,
                                          @RequestParam(required = false) Long sinceId,
                                          @RequestParam(required = false) Long maxId,
                                          RedirectAttributes redirectAttributes)
            throws TwitterException {

        Contest contest = getContest(contestCode, null);
        twitterService.processAdditionalTweets(hashtags, count, pages, sinceId, maxId, contest);

        successMessage(redirectAttributes, "twitter.fetch");

        return "redirect:/private/" + getContestURL(contestCode) + "/twitter";
    }
}
