package com.petrpopov.opennlprus.service;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    private String url;

    public void start(String startUrl) throws Exception {

        url = startUrl;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlLocation);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);


        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed(startUrl);

        controller.start(SimpleCrawler.class, numberOfCrawlers);
    }


    public String getUrl() {
        return url;
    }
}
