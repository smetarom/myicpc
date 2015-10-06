package com.myicpc.social.rss;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.rss.RSSFeed;
import com.myicpc.model.rss.RSSMessage;
import com.myicpc.repository.rss.RSSFeedRepository;
import com.myicpc.repository.rss.RSSMessageRepository;
import com.myicpc.service.exception.WebServiceException;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class RssService {

    @Autowired
    private RSSFeedRepository rssFeedRepository;

    @Autowired
    private RSSMessageRepository rssMessageRepository;

    /**
     * Find all available RSS feeds
     *
     * @return RSS feeds
     */
    public Iterable<RSSFeed> findFeeds(final Contest contest) {
        Iterable<RSSFeed> rssFeeds = rssFeedRepository.findByContestOrderByNameAsc(contest);
        for (RSSFeed rssFeed : rssFeeds) {
            Long count = rssMessageRepository.countByRSSFeed(rssFeed);
            rssFeed.setMessageCount(count.intValue());
        }
        return rssFeeds;
    }

    /**
     * Creates RSS feed
     *
     * @param rssFeed RSS feed to be persisted
     * @throws WebServiceException connecting the RSS feed failed
     * @throws MalformedURLException RSS feed URL is not valid
     */
    public void createRssFeed(RSSFeed rssFeed) throws WebServiceException, MalformedURLException {
        try {
            URL feedUrl = new URL(rssFeed.getUrl());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            rssFeed.setLinkToSource(feed.getLink());
            rssFeed.setDisabled(false);

            rssFeedRepository.save(rssFeed);
        } catch (MalformedURLException e) {
            throw e;
        } catch (FeedException | IOException e) {
            throw new WebServiceException(e);
        }
    }

    /**
     * Pulls RSS messages from all feeds
     *
     * @throws WebServiceException
     */
    public void pullFeeds(Contest contest) throws WebServiceException {
        Iterable<RSSFeed> feeds = rssFeedRepository.findByContest(contest);

        for (RSSFeed rssFeed : feeds) {
            pullFeed(rssFeed);
        }
    }

    /**
     * Pulls RSS messages from feed
     *
     * @param rssFeedId
     *            RSS feed ID
     * @throws WebServiceException
     */
    public RSSFeed pullFeed(final Long rssFeedId) throws WebServiceException {
        RSSFeed rssFeed = rssFeedRepository.findOne(rssFeedId);
        return pullFeed(rssFeed);
    }

    /**
     * Pulls RSS messages from feed
     *
     * @param rssFeed
     * @throws WebServiceException
     */
    public RSSFeed pullFeed(final RSSFeed rssFeed) throws WebServiceException {
        if (rssFeed.isDisabled()) {
            return null;
        }
        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(rssFeed.getUrl())));
            Calendar calendar = new GregorianCalendar(rssFeed.getContest().getContestSettings().getYear(), 1, 1);
            for (Object oEntry : feed.getEntries()) {
                SyndEntry entry = (SyndEntry) oEntry;
                if (rssFeed.getLastPulledDate() != null && entry.getPublishedDate().before(rssFeed.getLastPulledDate())) {
                    continue;
                }
                if (calendar.getTime().after(entry.getPublishedDate())) {
                    continue;
                }
                RSSMessage message = new RSSMessage();
                message.setName(entry.getTitle());
                message.setAuthorEmail(entry.getAuthor());
                message.setPublishDate(entry.getPublishedDate());
                message.setRssFeed(rssFeed);
                message.setText(entry.getDescription().getValue());
                message.setUrl(entry.getLink());

                rssMessageRepository.save(message);
            }

            rssFeed.setLastPulledDate(new Date());
            return rssFeedRepository.save(rssFeed);
        } catch (Throwable e) {
            throw new WebServiceException(e);
        }
    }

    /**
     * Toggle the disable state of RSS feed
     *
     * @param rssFeed feed to have the disable state toggled
     */
    public void toogleDisableRSSFeed(final RSSFeed rssFeed) {
        rssFeed.setDisabled(!rssFeed.isDisabled());
        rssFeedRepository.save(rssFeed);
    }

}
