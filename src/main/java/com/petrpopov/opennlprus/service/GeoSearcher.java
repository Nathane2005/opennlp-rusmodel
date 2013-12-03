package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.dao.GeoWebTextDao;
import com.petrpopov.opennlprus.dao.WebTextDao;
import com.petrpopov.opennlprus.entity.WebText;
import com.petrpopov.opennlprus.other.ParseMessage;
import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
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

    @Autowired
    private GeoWebTextDao geoWebTextDao;

    @Autowired
    private WebTextDao webTextDao;

    private Logger logger = Logger.getLogger(GeoSearcher.class);

    public void uberFuckingMethod() throws IOException, InvalidTokenOffsetsException {

        List<WebText> list = webTextDao.findAll();
        for (WebText webText : list) {
            if(webText.getText().length() >= 200 )
                continue;
            if( webText.getText().contains("\n"))
                continue;

            luceneService.addDocument(webText.getUrl(), webText.getNumber(), webText.getText());
        }


        luceneService.searchGeo("Непал");
    }

    public synchronized int containsGeoName(ParseMessage message) throws IOException, ParseException {

        logger.info("Searching for geo locations in: " + message);
        return containsGeoName(message.getMessageUrl().getUrl(), message.getText());
    }

    public synchronized int containsGeoName(String url, String text) throws IOException, ParseException {

        int count = 0;
        List<String> found = new ArrayList<String>();

        for (String address : addressService.getAddresses()) {

            logger.debug("Using address for search: " + address);

            List<String> list = luceneService.search(address, "text");
            if( list.isEmpty() )
                continue;

            for (String url1 : list) {
                if( url1.equals(url) ) {
                    found.add(address);
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
