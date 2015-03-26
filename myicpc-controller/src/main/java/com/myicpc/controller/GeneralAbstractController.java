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
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Roman Smetana
 */
public abstract class GeneralAbstractController {
    private static final Logger logger = LoggerFactory.getLogger(GeneralAbstractController.class);

    @Autowired
    private ContestService contestService;

    protected Contest getContest(String contestCode, Model model) {
        Contest contest = contestService.getContest(contestCode);
        if (model != null) {
            model.addAttribute("contest", contest);
            model.addAttribute("contestURL", StringUtils.isEmpty(contestCode) ? "" : "/" + contestCode);
        }
        return contest;
    }

    protected String getContestURL(String contestCode) {
        return StringUtils.isEmpty(contestCode) ? "" : "/" + contestCode;
    }

    /**
     * Handles {@link Exception}
     *
     * @param ex exception
     * @return model and view of exception
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        logger.error("Error occured", ex);
        ModelAndView modelAndView = new ModelAndView("errorException");
        modelAndView.addObject("exception", ex);
        extendExceptionHandling(modelAndView);
        return modelAndView;
    }

    /**
     * Handles {@link ModuleDisabledException}
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
     * Handles {@link Exception}
     *
     * @param ex exception
     * @return model and view of exception
     */
    @ExceptionHandler(ContestNotFoundException.class)
    public ModelAndView handleException(ContestNotFoundException ex) {
        logger.error("Error Contest Not Found", ex);
        ModelAndView modelAndView = new ModelAndView("error/contestNotFound");
        modelAndView.addObject("exception", ex);
        extendExceptionHandling(modelAndView);
        return modelAndView;
    }

    /**
     * Add attributes to the model
     *
     * @param modelAndView
     */
    protected void extendExceptionHandling(ModelAndView modelAndView) {

    }

    /**
     * Empty string into null
     *
     * @param binder
     */
    @InitBinder
    public void trimStringBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    protected String getMessage(final String key) {
        return MessageUtils.getMessage(key);
    }

    protected String getMessage(final String key, final Object... params) {
        return MessageUtils.getMessage(key, params);
    }

    protected void redirectMessage(final RedirectAttributes redirectAttributes, final String attribute, final String key, final Object... params) {
        redirectAttributes.addFlashAttribute(attribute, getMessage(key, params));
    }

    protected void redirectMessage(final RedirectAttributes redirectAttributes, final String attribute, final String key) {
        redirectAttributes.addFlashAttribute(attribute, getMessage(key));
    }

    protected void successMessage(final RedirectAttributes redirectAttributes, final String key, final Object... params) {
        redirectMessage(redirectAttributes, "infoMsg", key, params);
    }

    protected void successMessage(final RedirectAttributes redirectAttributes, final String key) {
        redirectMessage(redirectAttributes, "infoMsg", key);
    }

    protected void errorMessage(final RedirectAttributes redirectAttributes, final String key, final Object... params) {
        redirectMessage(redirectAttributes, "errorMsg", key, params);
    }

    protected void errorMessage(final RedirectAttributes redirectAttributes, final String key) {
        redirectMessage(redirectAttributes, "errorMsg", key);
    }

    protected void errorMessage(final RedirectAttributes redirectAttributes, final Throwable ex) {
        redirectAttributes.addFlashAttribute("errorMsg", ex.getMessage());
    }

}
