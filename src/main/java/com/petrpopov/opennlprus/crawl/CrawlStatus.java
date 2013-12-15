package com.petrpopov.opennlprus.crawl;

import org.springframework.stereotype.Component;

/**
 * Created by petrpopov on 15.12.13.
 */

@Component
public class CrawlStatus {

    private volatile boolean stopped = false;

    public synchronized boolean isStopped() {
        return stopped;
    }

    public synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
