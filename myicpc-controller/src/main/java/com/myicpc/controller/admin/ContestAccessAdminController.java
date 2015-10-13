package com.myicpc.controller.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.UserContestAccess;
import com.myicpc.repository.security.SystemUserRepository;
import com.myicpc.repository.security.UserContestAccessRepository;
import com.myicpc.service.contest.ContestAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for contest access management
 * <p/>
 * It lists the contest managers and allows the user to add/delete contest access
 *
 * @author Roman Smetana
 */
@Controller
public class ContestAccessAdminController extends GeneralAdminController {

    @Autowired
    private ContestAccessService contestAccessService;

    @Autowired
    private UserContestAccessRepository userContestAccessRepository;

    @Autowired
    private SystemUserRepository systemUserRepository;


    @RequestMapping(value = "/private/{contestCode}/access", method = RequestMethod.GET)
    public String accessList(@PathVariable String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("contestAccesses", userContestAccessRepository.findByContest(contest, new Sort(Sort.Direction.ASC, "systemUser.username")));
        List<SystemUser> availableManagers = systemUserRepository.findAvailableContestManagers(contest, new Sort(Sort.Direction.ASC, "lastname", "firstname"));
        model.addAttribute("availableManagers", availableManagers);
        return "private/contest/contestAccess";
    }

    @RequestMapping(value = "/private/{contestCode}/access", method = RequestMethod.POST)
    public String accessList(@RequestParam(required = false) Long availableManagerId, @PathVariable String contestCode, RedirectAttributes redirectAttributes) {
        UserContestAccess userContestAccess = null;
        if (availableManagerId != null) {
            Contest contest = getContest(contestCode, null);
            SystemUser systemUser = systemUserRepository.findOne(availableManagerId);
            userContestAccess = contestAccessService.addContestAccess(systemUser, contest);
        }

        if (userContestAccess != null) {
            successMessage(redirectAttributes, "save.success");
        } else {
            errorMessage(redirectAttributes, "save.failed");
        }

        return "redirect:/private" + getContestURL(contestCode) + "/access";
    }

    @RequestMapping(value = "/private/{contestCode}/access/{contestAccessId}/delete", method = RequestMethod.GET)
    public String deleteContestAccess(@PathVariable Long contestAccessId, @PathVariable String contestCode, RedirectAttributes redirectAttributes) {
        contestAccessService.deleteContestAccess(contestAccessId);
        successMessage(redirectAttributes, "delete.success");
        return "redirect:/private" + getContestURL(contestCode) + "/access";
    }


}
