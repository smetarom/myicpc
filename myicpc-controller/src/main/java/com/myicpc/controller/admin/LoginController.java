package com.myicpc.controller.admin;

import com.myicpc.controller.GeneralAdminController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for security management
 * <p/>
 * It takes care about handling login, logout, and access without required permissions
 *
 * @author Roman Smetana
 */
@Controller
public class LoginController extends GeneralAdminController {
    /**
     * Shows a login page to administration
     *
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/login", method = RequestMethod.GET)
    public String login(final Model model) {
        return "private/login";
    }

    /**
     * Complete user logout and redirects to login page
     *
     * @return view
     */
    @RequestMapping(value = "/private/logout")
    public String logout(final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("logoutSuccess", true);
        return "redirect:/private/login";
    }

    /**
     * Redirects to login page after login failed
     *
     * @param redirectAttributes
     * @return redirect to login page
     */
    @RequestMapping(value = "/private/loginfailed", method = RequestMethod.GET)
    public String loginerror(final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", true);
        return "redirect:/private/login";
    }

    /**
     * Shows a page with information, that you don't have permission to view the
     * content
     *
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/access-denied", method = RequestMethod.GET)
    public String accessDenied(final Model model) {
        return "error/accessDenied";
    }
}
