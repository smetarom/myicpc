package com.myicpc.controller;

import com.myicpc.model.contest.Contest;
import com.myicpc.service.contest.ContestService;
import com.myicpc.service.email.EmailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for public landing page
 * <p/>
 * It shows the landing page and manages the feedback form
 *
 * @author Roman Smetana
 */
@Controller
public class HomeController extends GeneralController {
    @Autowired
    private ContestService contestService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("contests", contestService.getActiveContests());
        return "home";
    }

    @RequestMapping(value = "/{contestCode}/feedback-form", method = RequestMethod.GET)
    public String feedback(Model model, @PathVariable String contestCode) {
        getContest(contestCode, model);

        return "feedback/feedbackForm";
    }

    @RequestMapping(value = "/{contestCode}/feedback-form", method = RequestMethod.POST)
    public String processFeedback(@PathVariable String contestCode,
                                  @RequestParam(value = "feedbackEmail", required = false) String feedbackEmail,
                                  @RequestParam(value = "url", required = false) String url,
                                  @RequestParam(value = "feedbackMsg", required = false) String message,
                                  @RequestParam(value = "exceptionMessage", required = false) String exceptionMessage) {
        if (!StringUtils.isEmpty(url)) {
            // Spam detected, users don't see URL filed in the form
            return "redirect:" + getContestURL(contestCode);
        }
        Contest contest = getContest(contestCode, null);
        String subject = "Feedback for " + contest.getName();
        String msg = "";
        if (feedbackEmail != null && !feedbackEmail.isEmpty()) {
            msg += "From user: " + feedbackEmail + "\n\n";
        }
        if (!StringUtils.isEmpty(exceptionMessage)) {
            msg += "Exception: " + exceptionMessage + "\n\n";
        }
        if (!StringUtils.isEmpty(message)) {
            msg += message;
        }
        emailService.sendFeedbackEmail(contest, subject, msg);

        return "redirect:" + getContestURL(contestCode);
    }
}
