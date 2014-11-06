package com.myicpc.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller responsible for offline pages
 * <p/>
 * They are loaded automatically by HTML 5 offline feature defined in
 * cache.manifest
 *
 * @author Roman Smetana
 */
@Controller
public class OfflineController extends GeneralController {

    /**
     * Returns HTML 5 offline manifest
     */
    @RequestMapping(value = "/loadCacheManifest", method = RequestMethod.GET)
    public String cacheManifest() {
        return "offline/loadCacheManifest";
    }

    /**
     * Return HTML5 offline manifest
     *
     * @param model
     * @param request
     * @return view
     */
    @RequestMapping(value = "/cache.manifest", method = RequestMethod.GET)
    public String manifest(Model model, HttpServletRequest request) {

        model.addAttribute("ctx", request.getContextPath());
        return "offline/cacheManifest";
    }

    /**
     * Shows offline homepage
     *
     * @return view
     */
    @RequestMapping(value = "/offline", method = RequestMethod.GET)
    public String offline() {
        return "offline/offline";
    }

    /**
     * Shows offline scoreboard
     *
     * @return view
     */
    @RequestMapping(value = "/offline/scoreboard", method = RequestMethod.GET)
    public String offlineScoreboard() {
        return "offline/offlineScoreboard";
    }

    /**
     * Shows offline schedule
     *
     * @return view
     */
    @RequestMapping(value = "/offline/schedule", method = RequestMethod.GET)
    public String offlineSchedule() {
        return "offline/offlineSchedule";
    }

    /**
     * Shows offline quest
     *
     * @return view
     */
    @RequestMapping(value = "/offline/quest", method = RequestMethod.GET)
    public String offlineQuest() {
        return "offline/offlineQuest";
    }

}
