package com.petrpopov.opennlprus.main;

import com.petrpopov.opennlprus.dao.AddressDao;
import com.petrpopov.opennlprus.entity.Address;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: petrpopov
 * Date: 26.11.13
 * Time: 21:30
 */
public class App {

    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "spring.xml" }, true);


        AddressDao addressDao = context.getBean(AddressDao.class);
        Address address = addressDao.find("00000e97-4287-4b0e-ac97-346ca29e5f39");

        System.out.println(address);

        //CrawlerManager manager = new CrawlerManager();
        //manager.start("http://www.rbc.ru/");
    }
}