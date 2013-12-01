package com.petrpopov.opennlprus.service;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: petrpopov
 * Date: 26.11.13
 * Time: 21:34
 */

@Component
public class CrawlerManager {

    @Value("${crawl_location}")
    private String crawlLocation;

    @Value("${crawl_numbers}")
    private Integer numberOfCrawlers;

    private List<String> urls;

    private CrawlController controller;

    private volatile boolean stopped = false;

    public void start(String url) throws Exception {
        List<String> urls = new ArrayList<String>();
        urls.add(url);
        start(urls);
    }

    public void start(List<String> urls) throws Exception {

        this.urls = urls;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlLocation);
      //  config.setMaxPagesToFetch(2);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);


        controller = new CrawlController(config, pageFetcher, robotstxtServer);
        for (String url : urls) {
            controller.addSeed(url);
        }

        controller.start(SimpleCrawler.class, numberOfCrawlers);
    }

    public synchronized void stop() {
        if( controller == null )
            return;

        if( stopped )
            return;

        stopped=true;
        controller.shutdown();
    }

    public synchronized boolean isStopped() {
        return stopped;
    }

    public List<String> getUrls() {
        return urls;
    }
}
