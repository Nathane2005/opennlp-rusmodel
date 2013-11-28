package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.other.SpringContext;
import com.petrpopov.opennlprus.other.WebMessage;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
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

    private String url;

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

        this.url = manager.getUrl();
        if( this.url == null )
            throw new RuntimeException("No url in CrawlerManager !");
    }

    @Override
    public boolean shouldVisit(WebURL url) {

        String href = url.getURL().toLowerCase();
        boolean res = !FILTERS.matcher(href).matches() && href.startsWith(this.url);
        return res;
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            List<WebURL> links = htmlParseData.getOutgoingUrls();

            System.out.println("Text length: " + text.length());
            System.out.println("Html length: " + html.length());
            System.out.println("Number of outgoing links: " + links.size());

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


