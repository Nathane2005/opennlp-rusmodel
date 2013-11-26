package com.petrpopov.opennlprus;

/**
 * User: petrpopov
 * Date: 26.11.13
 * Time: 21:30
 */
public class App {

    public static void main(String[] args) throws Exception {

        CrawlerManager manager = new CrawlerManager();
        manager.start("http://www.rbc.ru/");
    }
}
