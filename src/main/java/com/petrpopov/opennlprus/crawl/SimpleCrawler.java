package com.petrpopov.opennlprus.crawl;

import com.petrpopov.opennlprus.dto.WebMessage;
import com.petrpopov.opennlprus.util.OpException;
import com.petrpopov.opennlprus.util.SpringContext;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Pattern;

/**
 * User: petrpopov
 * Date: 26.11.13
 * Time: 21:32
 */
public class SimpleCrawler extends WebCrawler {

    private List<String> urls;

    private Logger logger = Logger.getLogger(SimpleCrawler.class);

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    public SimpleCrawler() {
        //stupid fucking macaroni code
        //because of stupid fucking hardcode newInstance in fucking crawler4j
        CrawlerManager manager = SpringContext.getApplicationContext().getBean(CrawlerManager.class);
        if( manager == null )
            throw new RuntimeException("No fucking SpringContext initialized !");

        this.urls = manager.getUrls();
        if( this.urls == null )
            throw new RuntimeException("No urls in CrawlerManager !");

        if( this.urls.isEmpty() )
            throw new RuntimeException("No urls in CrawlerManager !");
    }

    @Override
    public boolean shouldVisit(WebURL webUrl) {

        boolean stopped = isStopped();
        if( stopped ) {
            return false;
        }

        String href = webUrl.getURL().toLowerCase();

        boolean b = !FILTERS.matcher(href).matches();
        for (String url : this.urls) {
            boolean res = b && href.startsWith(url);
            if( res )
                return res;
        }

        return false;
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        logger.info("Visiting URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            //List<WebURL> links = htmlParseData.getOutgoingUrls();

            try {
                processMessage(htmlParseData, url);
            } catch (OpException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processMessage(HtmlParseData htmlParseData, String url) throws OpException {

        String text = htmlParseData.getText();
        String html = htmlParseData.getHtml();

        WebMessage message = new WebMessage(url, text, html);

        CrawlProcessing processing = SpringContext.getApplicationContext().getBean(CrawlProcessing.class);
        if( processing != null )
            processing.proccessMessage(message);
    }


    private boolean isStopped() {

        CrawlStatus status = SpringContext.getApplicationContext().getBean(CrawlStatus.class);
        if( status != null ) {
            boolean stopped = status.isStopped();
            return stopped;
        }

        return false;
    }
}



