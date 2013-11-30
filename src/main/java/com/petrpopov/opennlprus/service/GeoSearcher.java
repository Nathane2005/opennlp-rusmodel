package com.petrpopov.opennlprus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: petrpopov
 * Date: 29.11.13
 * Time: 0:35
 */

@Component
public class GeoSearcher {


    @Autowired
    private AddressService addressService;
    /*
    public boolean containsGeoName(WebMessage message) throws IOException, ParseException {

       for (Address address : addressService.getAddresses()) {

            if( address.getId().equals("5c8b06f1-518e-496e-b683-7bf917e0d70b"))
                System.out.println("moscow");

            /*
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

            return ok;*/

  /*
        return false;
    }*/
}
