package com.myicpc.controller.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.service.contest.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes("contest")
public class DashboardAdminController extends GeneralAdminController {
    @Autowired
    private ContestService contestService;

    @RequestMapping(value = {"/private", "/private/home"}, method = RequestMethod.GET)
    public String home(final Model model, final HttpServletRequest request) {
        model.addAttribute("contests", contestService.getActiveContestsSecured());
        return "private/home";
    }
}
