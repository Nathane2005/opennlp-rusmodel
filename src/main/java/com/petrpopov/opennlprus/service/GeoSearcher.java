package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.entity.Address;
import com.petrpopov.opennlprus.other.WebMessage;
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
    private LuceneService luceneService;

    @Autowired
    private AddressService addressService;

    public boolean containsGeoName(WebMessage message) throws IOException, ParseException {

        for (Address address : addressService.getAddresses()) {

            List<String> list = luceneService.search(address.getFormalname(), "text");
            if( list.isEmpty() )
                return false;

            boolean ok = false;
            for (String url : list) {
                if( url.equals(message.getUrl()) ) {
                    ok = true;
                    break;
                }
            }

            return ok;
        }

        return false;
    }
}
