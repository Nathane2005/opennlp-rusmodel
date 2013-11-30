package com.petrpopov.opennlprus.main;

import com.petrpopov.opennlprus.other.WebMessage;
import com.petrpopov.opennlprus.service.LuceneService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: petrpopov
 * Date: 26.11.13
 * Time: 21:30
 */
public class App {

    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"spring/spring.xml"}, true);


        WebMessage message = new WebMessage("http://ya.ru", "Висячий сад это уникаьлный по своей структуре " +
                "и природе жкспонат, который был создан в Московской области задолго до нашей эры древними греками " +
                "в полнолуние, после того, как они упоролись метамфетамином и водкой Журавли.");

        LuceneService bean = context.getBean(LuceneService.class);
        bean.addDocument(message);

        bean.search("Московская область", "text");

        //GeoSearcher searcher = context.getBean(GeoSearcher.class);
        //searcher.containsGeoName(message);

        // AddressDao addressDao = context.getBean(AddressDao.class);
       // Address address = addressDao.find("00000e97-4287-4b0e-ac97-346ca29e5f39");


       // CrawlerManager manager = context.getBean(CrawlerManager.class);
        //manager.start("http://www.rbc.ru/");
    }
}
