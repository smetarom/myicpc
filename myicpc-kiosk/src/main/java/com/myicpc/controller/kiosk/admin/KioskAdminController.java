package com.myicpc.controller.kiosk.admin;

import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.kiosk.KioskContent;
import com.myicpc.repository.kiosk.KioskContentRepository;
import com.myicpc.service.kiosk.KioskMngService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Admin controller for kiosk
 *
 * @author Roman Smetana
 */
@Controller
@SessionAttributes("kioskContent")
public class KioskAdminController extends GeneralAdminController {
    @Autowired
    private KioskMngService kioskMngService;

    @Autowired
    private KioskContentRepository kioskContentRepository;

    /**
     * List of custom HTML pages for kiosk
     *
     * @param model
     * @param contestCode
     * @return JSP page
     */
    @RequestMapping(value = "/private/{contestCode}/kiosk", method = RequestMethod.GET)
    public String kiosk(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("contentList", kioskContentRepository.findByContest(contest, new Sort(Sort.Direction.ASC, "name")));

        return "private/kiosk/kiosk";
    }

    /**
     * Creates a new HTML custom kiosk page
     *
     * @param contestCode
     * @param model
     * @return JSP view
     */
    @RequestMapping(value = "/private/{contestCode}/kiosk/content/create", method = RequestMethod.GET)
    public String createKioskContent(@PathVariable String contestCode, final Model model) {
        Contest contest = getContest(contestCode, model);

        KioskContent kioskContent = new KioskContent();
        kioskContent.setContest(contest);
        model.addAttribute("kioskContent", kioskContent);
        model.addAttribute("mode", "create");
        model.addAttribute("headlineTitle", getMessage("kioskContentAdmin.create"));
        return "private/kiosk/editKioskContent";
    }

    /**
     * Edits HTML custom kiosk page
     *
     * @param contestCode
     * @param kioskContentId     kiosk content ID
     * @param model
     * @param redirectAttributes
     * @return JSP view
     */
    @RequestMapping(value = "/private/{contestCode}/kiosk/content/{kioskContentId}/edit", method = RequestMethod.GET)
    public String editPoll(@PathVariable final String contestCode, @PathVariable final Long kioskContentId, final Model model,
                           RedirectAttributes redirectAttributes) {
        getContest(contestCode, model);
        KioskContent kioskContent = kioskContentRepository.findOne(kioskContentId);

        if (kioskContent == null) {
            errorMessage(redirectAttributes, "kioskContentAdmin.notFound");
            return "redirect:/private" + getContestURL(contestCode) + "/kiosk";
        }

        model.addAttribute("kioskContent", kioskContent);
        model.addAttribute("mode", "edit");
        model.addAttribute("headlineTitle", getMessage("kioskContentAdmin.edit"));
        return "private/kiosk/editKioskContent";
    }

    /**
     * Deletes HTML custom kiosk page
     *
     * @param contestCode
     * @param kioskContentId     kiosk content ID
     * @param redirectAttributes
     * @return redirect to kiosk page list
     */
    @RequestMapping(value = "/private/{contestCode}/kiosk/content/{kioskContentId}/delete", method = RequestMethod.GET)
    public String deletePoll(@PathVariable final String contestCode, @PathVariable final Long kioskContentId, final RedirectAttributes redirectAttributes) {
        kioskContentRepository.delete(kioskContentId);
        successMessage(redirectAttributes, "delete.success");
        return "redirect:/private" + getContestURL(contestCode) + "/kiosk";
    }

    /**
     * Process HTML custom kiosk page update
     *
     * @param contestCode
     * @param kioskContent edited {@link KioskContent}
     * @param result
     * @param model
     * @return redirect to kiosk page list if successful. Stays on the page on validation error
     */
    @RequestMapping(value = "/private/{contestCode}/kiosk/content/update", method = RequestMethod.POST)
    public String updatePoll(@PathVariable String contestCode, @Valid @ModelAttribute("kioskContent") KioskContent kioskContent, BindingResult result, Model model) {
        Contest contest = getContest(contestCode, model);
        model.addAttribute("headlineTitle", getMessage("pollAdmin.edit"));
        if (result.hasErrors()) {
            return "private/kiosk/editKioskContent";
        }

        kioskMngService.updateKioskContent(kioskContent);
        return "redirect:/private" + getContestURL(contestCode) + "/kiosk";
    }

    @RequestMapping(value = "/private/{contestCode}/kiosk/change-mode", method = RequestMethod.GET)
    public String kioskChangeMode(Model model, @PathVariable String contestCode) {
        Contest contest = getContest(contestCode, model);
        kioskMngService.changeKioskMode(contest);
        return "redirect:/private" + getContestURL(contestCode) + "/kiosk";
    }

}
