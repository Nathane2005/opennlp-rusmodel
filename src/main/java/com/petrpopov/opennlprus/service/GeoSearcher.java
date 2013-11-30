package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.entity.Address;
import com.petrpopov.opennlprus.other.WebMessage;
import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

    public synchronized int containsGeoName(WebMessage message) throws IOException, ParseException {

        logger.info("Searching for geo locations in: " + message);

        int count = 0;
        for (Address address : addressService.getAddresses()) {

            logger.debug("Using addres for search: " + address);

            String name = address.getFormalname();
            name = name.replaceAll("[-]", " ").trim();

            List<String> list = luceneService.search(name, "text");
            if( list.isEmpty() )
                continue;

            for (String url : list) {
                if( url.equals(message.getUrl()) ) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }
}
