package com.myicpc.controller;

import com.myicpc.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Roman Smetana
 */
@Controller
public class TimelineController {
    @Autowired
    private ExampleService exampleService;

    @RequestMapping(value = {"/", "/timeline", "/private/timeline"}, method = RequestMethod.GET)
    public String timeline(Model model) {
        exampleService.helloWord();
        return "timeline";
    }
}
