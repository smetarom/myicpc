package com.myicpc.controller;

import com.myicpc.commons.utils.TimeUtils;
import com.myicpc.model.contest.Contest;
import com.myicpc.service.settings.GlobalSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * General controller, which is responsible for handling exceptions and populate
 * model with object, we want to have in all private views
 *
 * @author Roman Smetana
 */
public class GeneralAdminController extends GeneralAbstractController {
    private static final Logger logger = LoggerFactory.getLogger(GeneralAdminController.class);

    @Autowired
    private GlobalSettingsService globalSettingsService;

    @Override
    protected Contest getContest(String contestCode, Model model) {
        Contest contest = super.getContest(contestCode, model);
        if (model != null && contest != null) {
            model.addAttribute("currentDate", TimeUtils.convertUTCDateToLocal(new Date(), contest.getTimeDifference()));
            model.addAttribute("adminContact", globalSettingsService.getGlobalSettings().getAdminEmail());
        }
        return contest;
    }

    /**
     * Exception handling of {@link OptimisticLockException}
     *
     * @param ex      exception
     * @param request
     * @return model and view for exception
     */
    @ExceptionHandler({OptimisticLockException.class, JpaOptimisticLockingFailureException.class})
    public ModelAndView handleOptimisticLockException(final OptimisticLockException ex, final HttpServletRequest request) {
        logger.error("OptimisticLockException occured", ex);

        ModelAndView modelAndView = new ModelAndView("forward:/private/home");
        modelAndView.addObject("errorMsg", getMessage("error.optimisticLock") + " <a href=\"javascript:window.history.back()\">Go back</a>");
        return modelAndView;
    }

    /**
     * Bind a custom date format yyyy-MM-dd HH:mm:ss
     *
     * @param binder
     */
    public void bindDateTimeFormat(final WebDataBinder binder) {
        bindDate(binder, TimeUtils.getDateTimeFormat(), false);
    }

    /**
     * Bind a custom date format yyyy-MM-dd HH:mm:ss, or allow empty date
     *
     * @param binder
     */
    public void bindDateTimeFormatAllowEmpty(final WebDataBinder binder) {
        bindDate(binder, TimeUtils.getDateTimeFormat(), true);
    }

    /**
     * Bind a custom date format yyyy-MM-dd
     *
     * @param binder
     */
    public void bindDateFormat(final WebDataBinder binder) {
        bindDate(binder, TimeUtils.getDateFormat(), false);
    }

    /**
     * Bind a custom date format yyyy-MM-dd, or allow empty date
     *
     * @param binder
     */
    public void bindDateFormatAllowEmpty(final WebDataBinder binder) {
        bindDate(binder, TimeUtils.getDateFormat(), true);
    }

    /**
     * Bind a custom date format
     *
     * @param binder     binder
     * @param dateFormat DateFormat to use for parsing and rendering
     * @param allowEmpty if empty strings should be allowed
     */
    private void bindDate(WebDataBinder binder, SimpleDateFormat dateFormat, boolean allowEmpty) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, allowEmpty));
    }

    @Override
    protected void extendExceptionHandling(ModelAndView modelAndView) {
        super.extendExceptionHandling(modelAndView);
        modelAndView.addObject("privateMode", true);
    }
}
