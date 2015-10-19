package com.myicpc.controller;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.contest.ContestService;
import com.myicpc.service.exception.ContestNotFoundException;
import com.myicpc.service.exception.ModuleDisabledException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Parent class shared with all MyICPC controllers
 *
 * @author Roman Smetana
 */
public abstract class GeneralAbstractController {
    private static final Logger logger = LoggerFactory.getLogger(GeneralAbstractController.class);

    @Autowired
    protected ContestService contestService;

    /**
     * Returns {@link Contest} by {@code contestCode}
     * <p/>
     * It loads the contest by code and it puts it to
     * Spring MVC {@code model}
     *
     * @param contestCode contest code to be searched
     * @param model       model to be populated
     * @return contest with the code or {@code null} if not found
     */
    protected Contest getContest(String contestCode, Model model) {
        Contest contest = loadContest(contestCode);
        if (model != null) {
            model.addAttribute("contest", contest);
            model.addAttribute("contestURL", StringUtils.isEmpty(contestCode) ? "" : "/" + contestCode);
        }
        return contest;
    }

    /**
     * Loads {@link Contest} by {@code contestCode}
     *
     * @param contestCode contest code to be searched
     * @return contest with the code or {@code null} if not found
     */
    protected Contest loadContest(String contestCode) {
        return contestService.getContest(contestCode);
    }

    /**
     * Forms contest URL prefix from {@code contestCode}
     *
     * @param contestCode contest code to be used in URL
     * @return contest URL prefix or empty string, if no {@code contestCode} provided
     */
    protected String getContestURL(String contestCode) {
        return StringUtils.isEmpty(contestCode) ? "" : "/" + contestCode;
    }

    /**
     * Handles general {@link Exception}
     * <p/>
     * It shows the default error page
     *
     * @param ex exception
     * @return model and view of exception
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        logger.error("Error occured", ex);
        ModelAndView modelAndView = new ModelAndView("error/exception");
        modelAndView.addObject("exception", ex);
        extendExceptionHandling(modelAndView);
        return modelAndView;
    }

    /**
     * Handles {@link ModuleDisabledException}
     * <p/>
     * It shows the page with information about disabled module
     *
     * @param ex exception
     * @return model and view of exception
     */
    @ExceptionHandler(ModuleDisabledException.class)
    public ModelAndView handleModuleDisabledException(ModuleDisabledException ex) {
        ModelAndView modelAndView = new ModelAndView("errorDisabledModule");
        modelAndView.addObject("exception", ex);
        return modelAndView;
    }

    /**
     * Handles {@link ContestNotFoundException}
     * <p/>
     * It shows the error page that the contest does not exists
     *
     * @param ex exception
     * @return model and view of exception
     */
    @ExceptionHandler(ContestNotFoundException.class)
    public ModelAndView handleException(ContestNotFoundException ex) {
        logger.error("Error Contest Not Found" + ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error/contestNotFound");
        modelAndView.addObject("exception", ex);
        extendExceptionHandling(modelAndView);
        return modelAndView;
    }

    /**
     * Handles {@link AccessDeniedException}
     * <p/>
     * It shows the error page that the logged user does not have
     * the required permission to execute the action
     *
     * @param ex exception
     * @return model and view of exception
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("Access denied", ex);
        ModelAndView modelAndView = new ModelAndView("error/accessDenied");
        extendExceptionHandling(modelAndView);
        return modelAndView;
    }

    /**
     * Add attributes to the model on the exception
     *
     * @param modelAndView exception page model
     */
    protected void extendExceptionHandling(ModelAndView modelAndView) {
        // by default, do not put anything to model
    }

    /**
     * Empty string into null
     *
     * @param binder data binder
     */
    @InitBinder
    public void trimStringBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * Translates the translation key {@code key} into localized text
     *
     * @param key translation key
     * @return localized text
     */
    protected String getMessage(final String key) {
        return MessageUtils.getMessage(key);
    }

    /**
     * Translates the translation key {@code key} into localized text
     *
     * It provides {@code defaultText} used if the translation not found
     *
     * @param key translation key
     * @param defaultText default text used instead localized text
     * @return localized text
     */
    protected String getMessageWithDefault(final String key, final String defaultText) {
        return MessageUtils.getMessageWithDefault(key, defaultText);
    }

    /**
     * Translates the translation key {@code key} with parameters into localized text
     *
     * @param key translation key
     * @param params parameters for translation key
     * @return localized text
     */
    protected String getMessage(final String key, final Object... params) {
        return MessageUtils.getMessage(key, params);
    }

    /**
     * Localizes the text and put it to redirect model as {@code attribute}
     *
     * @param redirectAttributes redirect model
     * @param attribute variable name in redirect model
     * @param key translation key
     * @param params parameters in translation key
     */
    protected void redirectMessage(final RedirectAttributes redirectAttributes, final String attribute, final String key, final Object... params) {
        redirectAttributes.addFlashAttribute(attribute, getMessage(key, params));
    }

    /**
     * Localizes the text and put it to redirect model as {@code attribute}
     *
     * @param redirectAttributes redirect model
     * @param attribute variable name in redirect model
     * @param key translation key
     */
    protected void redirectMessage(final RedirectAttributes redirectAttributes, final String attribute, final String key) {
        redirectAttributes.addFlashAttribute(attribute, getMessage(key));
    }

    /**
     * Localizes the text and put it to redirect model as 'infoMsg'
     *
     * @param redirectAttributes redirect model
     * @param key translation key
     * @param params parameters in translation key
     */
    protected void successMessage(final RedirectAttributes redirectAttributes, final String key, final Object... params) {
        redirectMessage(redirectAttributes, "infoMsg", key, params);
    }

    /**
     * Localizes the text and put it to redirect model as 'infoMsg'
     *
     * @param redirectAttributes redirect model
     * @param key translation key
     */
    protected void successMessage(final RedirectAttributes redirectAttributes, final String key) {
        redirectMessage(redirectAttributes, "infoMsg", key);
    }

    /**
     * Localizes the text and put it to redirect model as 'errorMsg'
     *
     * @param redirectAttributes redirect model
     * @param key translation key
     * @param params parameters in translation key
     */
    protected void errorMessage(final RedirectAttributes redirectAttributes, final String key, final Object... params) {
        redirectMessage(redirectAttributes, "errorMsg", key, params);
    }

    /**
     * Localizes the text and put it to redirect model as 'errorMsg'
     *
     * @param redirectAttributes redirect model
     * @param key translation key
     */
    protected void errorMessage(final RedirectAttributes redirectAttributes, final String key) {
        redirectMessage(redirectAttributes, "errorMsg", key);
    }

    /**
     * Localizes the text and put it to redirect model as 'errorMsg'
     *
     * @param redirectAttributes redirect model
     * @param ex exception causes the error
     */
    protected void errorMessage(final RedirectAttributes redirectAttributes, final Throwable ex) {
        redirectAttributes.addFlashAttribute("errorMsg", ex.getMessage());
    }

}
