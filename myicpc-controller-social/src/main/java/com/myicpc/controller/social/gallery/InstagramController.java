package com.myicpc.controller.social.gallery;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.dto.GlobalSettings;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.service.settings.GlobalSettingsService;
import com.myicpc.social.gallery.InstagramService;
import com.myicpc.social.dto.InstagramUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Roman Smetana
 */
@Controller
public class InstagramController extends GeneralController {
    private static final Logger logger = LoggerFactory.getLogger(InstagramController.class);

    @Autowired
    private InstagramService instagramService;

    @Autowired
    private GlobalSettingsService globalSettingsService;

    @RequestMapping(value = "/secure/{contestCode}/task/instagram/subscribe", method = RequestMethod.GET)
    public String startSubscription(@PathVariable final String contestCode, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        GlobalSettings globalSettings = globalSettingsService.getGlobalSettings();
        String callbackUrl = globalSettings.getCallbackUrl() + "/subscription" + getContestURL(contestCode) + "/instagram";

        try {
            instagramService.startSubscription(contest, callbackUrl);
        } catch (URISyntaxException | IOException ex) {
            logger.error(ex.getMessage(), ex);
            // TODO add error message
        }

        return "redirect:/private" + getContestURL(contestCode);
    }

    @ResponseBody
    @RequestMapping(value = "/subscription/{contestCode}/instagram", method = RequestMethod.GET)
    public String handleInstagramChallenge(@PathVariable final String contestCode,
                                           @RequestParam("hub.mode") String mode,
                                           @RequestParam("hub.challenge") String challenge,
                                           @RequestParam(value = "hub.verify_token", required = false) String token) {
        if ("subscribe".equalsIgnoreCase(mode)) {
            return challenge;
        }
        return null;
    }

    @RequestMapping(value = "/subscription/{contestCode}/instagram", method = RequestMethod.POST, consumes="application/json")
    public void handleInstagramUpdate(@PathVariable final String contestCode, @RequestBody InstagramUpdate[] updates) {
        System.out.println("instagram update");
        for (InstagramUpdate update : updates) {
            System.out.println(update);
        }
    }

    @RequestMapping(value = "{contestCode}/instagram/new", method = RequestMethod.GET)
    public String getNew(@PathVariable final String contestCode, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        try {
            instagramService.getNewPosts(contest);
        } catch (WebServiceException e) {
            e.printStackTrace();
        }
        return null;
    }
}
