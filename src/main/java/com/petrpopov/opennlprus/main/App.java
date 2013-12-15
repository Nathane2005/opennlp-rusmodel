package com.petrpopov.opennlprus.main;

import com.petrpopov.opennlprus.crawl.CrawlerManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * User: petrpopov
 * Date: 26.11.13
 * Time: 21:30
 */
public class App {

    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"spring/applicationContext.xml"}, true);
        //GeoSearcher searcher = context.getBean(GeoSearcher.class);

        //searcher.uberFuckingMethod();

      /*
        GeoWebTextDao bean = context.getBean(GeoWebTextDao.class);
        List<GeoWebText> all = bean.findAll();

        FileWriter fstream = new FileWriter("/Users/petrpopov/Downloads/modeltest.txt", true);
        BufferedWriter out = new BufferedWriter(fstream);

        //Close the output stream


        for (GeoWebText geoWebText : all) {
            String text = geoWebText.getText();
            out.write(text);
            out.newLine();
        }

        out.close();  */


        List<String> urls = new ArrayList<String>();
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
        manager.start(urls);
    }
}
