package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.entity.Address;
import com.petrpopov.opennlprus.other.ParseMessage;
import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: petrpopov
 * Date: 29.11.13
 * Time: 0:35
 */

@Component
public class GeoSearcher {

    @Autowired
    private AddressService addressService;

    @Autowired
    private LuceneService luceneService;

    private Logger logger = Logger.getLogger(GeoSearcher.class);

    public synchronized int containsGeoName(ParseMessage message) throws IOException, ParseException {

        logger.info("Searching for geo locations in: " + message);
        return containsGeoName(message.getMessageUrl().getUrl(), message.getText());
    }

    public synchronized int containsGeoName(String url, String text) throws IOException, ParseException {

        int count = 0;
        List<String> found = new ArrayList<String>();

        for (Address address : addressService.getAddresses()) {

            logger.debug("Using address for search: " + address);

            String name = address.getFormalname();
            name = name.replaceAll("[-]", " ").trim();

            List<String> list = luceneService.search(name, "text");
            if( list.isEmpty() )
                continue;

            for (String url1 : list) {
                if( url1.equals(url) ) {
                    found.add(name);
                    count++;
                    break;
                }
            }
        }

        if( count > 0 )
            logger.info("FOUND!!!");

        return count;
    }
}
