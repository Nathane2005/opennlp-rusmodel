package com.petrpopov.opennlprus.main;

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


        // LuceneService bean = context.getBean(LuceneService.class);
       // bean.addDocument(new WebMessage("http://ya.ru", "Hello, world, I am the CEO, bitch!"));

        // AddressDao addressDao = context.getBean(AddressDao.class);
       // Address address = addressDao.find("00000e97-4287-4b0e-ac97-346ca29e5f39");


       // CrawlerManager manager = context.getBean(CrawlerManager.class);
        //manager.start("http://www.rbc.ru/");
    }
}
