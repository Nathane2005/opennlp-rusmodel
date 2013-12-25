package com.petrpopov.opennlprus.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by petrpopov on 25.12.13.
 */

@Component
public class SearchAnalyzer {

    @Autowired
    private AddressService addressService;

    @Autowired
    private SearchServerService searchServer;

    private Logger logger = Logger.getLogger(SearchAnalyzer.class);

    public void analyzeGeo() {

        List<String> addresses = addressService.getAddresses();


        for (String address : addresses) {
            logger.info("Searching for adress: " + address);

            String q = getQueryForAddress(address);
            List<String> search = searchServer.search(q, 50);
        }


    }

    private String getQueryForAddress(String address) {
        String q = "";
        String[] split = address.split(" ");
        if( split.length > 1 ) {

            for(int i = 0; i < split.length; i++) {
                q += split[i] + " ";
                if( i != split.length-1 ) {
                    q += "AND ";
                }
            }
        }
        else {
            q = address;
        }
        q = q.trim();

        return q;
    }

}
