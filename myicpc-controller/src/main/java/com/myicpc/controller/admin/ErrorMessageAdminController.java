package com.myicpc.controller.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.notification.ErrorMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for private landing page
 *
 * @author Roman Smetana
 */
@Controller
public class ErrorMessageAdminController extends GeneralAdminController {

    @Autowired
    private ErrorMessageService errorMessageService;

    @RequestMapping(value = "/private/errors", method = RequestMethod.GET)
    public String allErrors(final Model model) {

        model.addAttribute("errors", errorMessageService.getAllErrorMessages());
        return "private/errors/allErrors";
    }

    @RequestMapping(value = "/private/{contestCode}/errors", method = RequestMethod.GET)
    public String contestErrors(@PathVariable String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("errors", errorMessageService.getContestErrorMessages(contest));
        return "private/errors/contestErrors";
    }
}
