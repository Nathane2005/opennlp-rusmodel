package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.other.SpringContext;
import com.petrpopov.opennlprus.other.WebMessage;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
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
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            List<WebURL> links = htmlParseData.getOutgoingUrls();

            WebMessage message = getMessage(url, htmlParseData);
            sendMessage(message);
        }
    }

    public void sendMessage(final WebMessage message) {
        JmsTemplate template = getJmsTemplate();
        template.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(message);
            }
        });
    }

    private JmsTemplate getJmsTemplate() {
        JmsTemplate jmsTemplate = SpringContext.getApplicationContext().getBean(JmsTemplate.class);
        return jmsTemplate;
    }

    private WebMessage getMessage(String url, HtmlParseData page) {
        String text = page.getText();

        WebMessage mes = new WebMessage(url, text);
        return mes;
    }
}



