package com.myicpc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Roman Smetana
 */
@Controller
public class TimelineController {

    @RequestMapping(value = {"/", "/timeline", "/private/timeline"}, method = RequestMethod.GET)
    public String timeline(Model model) {
        return "timeline";
    }
}
