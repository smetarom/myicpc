package com.myicpc.controller.social.admin.rss;

import com.myicpc.controller.GeneralAdminController;
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
public class RssAdminController extends GeneralAdminController {
    private static final Logger logger = LoggerFactory.getLogger(RssAdminController.class);

    @Autowired
    private RssService rssService;

    @Autowired
    private RSSFeedRepository rssFeedRepository;

    @Autowired
    private RSSMessageRepository rssMessageRepository;

    @RequestMapping(value = "/private/{contestCode}/rss", method = RequestMethod.GET)
    public String rssList(@PathVariable String contestCode, Model model) {
        Contest contest = getContest(contestCode, model);

        RSSFeed newFeed = new RSSFeed();

        model.addAttribute("rssFeed", newFeed);
        model.addAttribute("rssFeeds", rssService.findFeeds(contest));

        return "private/rss/rss";
    }

    @RequestMapping(value = "/private/{contestCode}/rss", method = RequestMethod.POST)
    public String submitNewRssFeed(@PathVariable String contestCode, @ModelAttribute RSSFeed rssFeed, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);

        rssFeed.setContest(contest);
        try {
            rssService.createRssFeed(rssFeed);
            successMessage(redirectAttributes, "rssAdmin.success", rssFeed.getName());
        } catch (MalformedURLException e) {
            logger.error("Create RSS feed error", e);
            errorMessage(redirectAttributes, "rssAdmin.error.wrongURL", rssFeed.getUrl());
        } catch (WebServiceException e) {
            logger.error("Create RSS feed error", e);
            errorMessage(redirectAttributes, "rssAdmin.error");
        }

        return "redirect:/private" + getContestURL(contestCode) + "/rss";
    }

    /**
     * Processes all feed resfresh
     *
     * @return redirect to RSS home page
     * @throws WebServiceException
     */
    @RequestMapping(value = "/private/{contestCode}/rss/refresh", method = RequestMethod.GET)
    public String refreshFeeds(@PathVariable final String contestCode, RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestCode, null);
        try {
            rssService.pullFeeds(contest);
            successMessage(redirectAttributes, "rssAdmin.refreshAll.success");
        } catch (WebServiceException e) {
            logger.error("Refresh of all RSS feeds failed.", e);
            errorMessage(redirectAttributes, "rssAdmin.refreshAll.error");
        }
        return "redirect:/private" + getContestURL(contestCode) + "/rss";
    }

    /**
     * Process a refresh of a RSS feed
     *
     * @param feedId
     *            RSS feed ID
     * @return redirect to RSS detail page
     * @throws WebServiceException
     */
    @RequestMapping(value = "/private/{contestCode}/rss/{feedId}/refresh", method = RequestMethod.GET)
    public String refreshFeed(@PathVariable final String contestCode, @PathVariable Long feedId, RedirectAttributes redirectAttributes) {
        try {
            RSSFeed feed = rssService.pullFeed(feedId);
            if (feed != null) {
                successMessage(redirectAttributes, "rssAdmin.refresh.success", feed.getName());
            }
        } catch (WebServiceException e) {
            logger.error("Refresh of the RSS feed with ID " + feedId + " failed.", e);
            errorMessage(redirectAttributes, "rssAdmin.refreshAll.error");
        }
        return "redirect:/private" + getContestURL(contestCode) + "/rss";
    }

    /**
     * Toggles if the feed is enabled
     *
     * @param feedId
     *            RSS feed ID
     * @return redirect to RSS detail page
     */
    @RequestMapping(value = "/private/{contestCode}/rss/{feedId}/activate", method = RequestMethod.GET)
    public String activateFeed(@PathVariable final String contestCode, @PathVariable Long feedId, RedirectAttributes redirectAttributes) {
        RSSFeed feed = rssFeedRepository.findOne(feedId);
        rssService.toogleDisableRSSFeed(feed);
        successMessage(redirectAttributes, feed.isDisabled() ? "rssAdmin.disable.success" : "rssAdmin.enable.success", feed.getName());
        return "redirect:/private" + getContestURL(contestCode) + "/rss";
    }

    @RequestMapping(value = "/private/{contestCode}/rss/{feedId}/delete", method = RequestMethod.GET)
    public String deleteFeed(@PathVariable final String contestCode, @PathVariable Long feedId, RedirectAttributes redirectAttributes) {
        RSSFeed feed = rssFeedRepository.findOne(feedId);
        rssFeedRepository.delete(feed);
        successMessage(redirectAttributes, "rssAdmin.delete.success", feed.getName());
        return "redirect:/private" + getContestURL(contestCode) + "/rss";
    }

    /**
     * Shows a detail of RSS feed
     *
     * @param feedId
     *            RSS feed ID
     * @param model
     * @return view
     */
    @RequestMapping(value = "/private/{contestCode}/rss/{feedId}", method = RequestMethod.GET)
    public String feedDetail(@PathVariable final String contestCode, @PathVariable final Long feedId, final Model model) {
        getContest(contestCode, model);
        RSSFeed rssFeed = rssFeedRepository.findOne(feedId);

        model.addAttribute("rssFeed", rssFeed);
        model.addAttribute("feedMessages", rssMessageRepository.findByRssFeedOrderByPublishDateDesc(rssFeed));
        return "private/rss/rssFeedDetail";
    }

    /**
     * Processes a RSS message delete operation
     *
     * @param rssFeed
     *            RSS feed
     * @param messageId
     *            RSS message ID
     * @param model
     * @return redirect to RSS detail page
     */
    @RequestMapping(value = "/private/{contestCode}/rss/deleteMessage/{messageId}", method = RequestMethod.GET)
    public String deleteRSSMessage(@PathVariable final String contestCode, @PathVariable final Long messageId,
                                   final RedirectAttributes redirectAttributes) {
        RSSMessage rssMessage = rssMessageRepository.findOne(messageId);
        rssMessageRepository.delete(rssMessage);
        successMessage(redirectAttributes, "rssAdmin.message.delete.success");
        return "redirect:/private" + getContestURL(contestCode) + "/rss/" + rssMessage.getRssFeed().getId();
    }
}
