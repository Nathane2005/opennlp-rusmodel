package com.petrpopov.opennlprus.main;

import com.petrpopov.opennlprus.service.GeoSearcher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: petrpopov
 * Date: 26.11.13
 * Time: 21:30
 */
public class App {

    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"spring/applicationContext.xml"}, true);


        GeoSearcher searcher = context.getBean(GeoSearcher.class);

        searcher.uberFuckingMethod();

        //GeoSearcher searcher = context.getBean(GeoSearcher.class);
        //searcher.uberFuckingMethod();
       /* List<String> urls = new ArrayList<String>();
        urls.add("http://ria.ru/");
        urls.add("http://lenta.ru/");
        urls.add("http://rbc.ru/");
        urls.add("http://www.bfm.ru/");
        urls.add("http://www.gazeta.ru/");
        urls.add("http://www.bbc.co.uk/russian");
        urls.add("http://www.kp.ru/");
        urls.add("http://www.interfax.ru/");
        urls.add("http://dni.ru/");
        urls.add("http://www.rg.ru/");

        CrawlerManager manager = context.getBean(CrawlerManager.class);
        manager.start(urls); */
    }
}
