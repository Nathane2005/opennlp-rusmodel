package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.other.ParseMessage;
import com.petrpopov.opennlprus.other.WebMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petrpopov
 * Date: 01.12.13
 * Time: 1:04
 */

@Component
public class WebMessageAnalyzer {

    @Autowired
    private LuceneService luceneService;

    @Autowired
    private GeoSearcher geoSearcher;

    @Autowired
    private Splitter splitter;

    @Autowired
    private MessageKeeper messageKeeper;

    private Logger logger = Logger.getLogger(WebMessageAnalyzer.class);

    public void analyze(WebMessage webMessage) {

        try {
            List<ParseMessage> list = splitter.split(webMessage);
            for (ParseMessage parseMessage : list) {
                boolean contains = messageKeeper.contains(parseMessage);
                if( contains )
                    continue;

                logger.info("Message object is not in the index. Adding");
                messageKeeper.add(parseMessage);
                luceneService.addDocument(parseMessage);

                logger.info("Looking for geo locations in message");
                int count = geoSearcher.containsGeoName(parseMessage);

                if(count == 0)
                    continue;

                logger.info("Found " + count + " geo locations in message " + parseMessage );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
