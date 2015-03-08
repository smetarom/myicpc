package com.myicpc.controller.social.rss;

import com.myicpc.controller.GeneralController;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.rss.RSSFeed;
import com.myicpc.model.rss.RSSMessage;
import com.myicpc.repository.rss.RSSFeedRepository;
import com.myicpc.repository.rss.RSSMessageRepository;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.social.rss.RssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;

/**
 * @author Roman Smetana
 */
@Controller
@SessionAttributes({ "rssEvent" })
public class RssController extends GeneralController {
    private static final Logger logger = LoggerFactory.getLogger(RssController.class);

    @Autowired
    private RSSMessageRepository rssMessageRepository;

    @Autowired
    private RSSFeedRepository rssFeedRepository;

    /**
     * Shows all RSS messages
     *
     * @param model
     * @param pageable
     * @return view
     */
    @RequestMapping(value = "/{contestCode}/rss", method = RequestMethod.GET)
    public String preview(@PathVariable final String contestCode, Model model, Pageable pageable) {
        Contest contest = getContest(contestCode, model);

        model.addAttribute("rssMessagesPage", rssMessageRepository.findByRssFeedContestOrderByPublishDateDesc(contest, pageable));
        model.addAttribute("sideMenuActive", "rss");
        return "rss/rssFeed";
    }
}
