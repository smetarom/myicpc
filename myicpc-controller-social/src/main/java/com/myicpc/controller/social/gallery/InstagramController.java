package com.myicpc.controller.social.gallery;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.social.InstagramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/secure/{contestCode}/task/instagram/subscribe", method = RequestMethod.GET)
    public String editContest(@PathVariable final String contestCode, final RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        try {
            instagramService.startSubscription(contest, "localhost:8080/subscribe");
        } catch (URISyntaxException | IOException ex) {
            logger.error(ex.getMessage(), ex);
            // TODO add error message
//            errorMessage(redirectAttributes, "");
        }

        return "redirect:/private"+getContestURL(contestCode);
    }
}
