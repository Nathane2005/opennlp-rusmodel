package com.petrpopov.opennlprus.service;

import com.google.common.base.Strings;
import com.petrpopov.opennlprus.dao.AddressDao;
import com.petrpopov.opennlprus.entity.Address;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * User: petrpopov
 * Date: 29.11.13
 * Time: 0:40
 */

@Component
public class AddressService {

    private final String LEVEL_SHORT_1 = "край";
    private final String LEVEL_SHORT_2 = "обл";
    private final String LEVEL_SHORT_3 = "респ";
    private final String LEVEL_SHORT_4 = "округ";

    private final String LEVEL_LONG_2 = "область";
    private final String LEVEL_LONG_3 = "республика";

    @Autowired
    private AddressDao addressDao;

    private volatile List<String> addresses = new ArrayList<String>();

    private Logger logger = Logger.getLogger(AddressService.class);

    @PostConstruct
    public void init() {

        logger.info("Finding all major locations...");

        List<Address> all = addressDao.findAllMajor();
        for (Address address : all) {

            String fullName = address.getFormalname();
            String shortName = null;

            String regionType = address.getShortname();
            if( regionType.equalsIgnoreCase(LEVEL_SHORT_1)) {
                fullName += " " + LEVEL_SHORT_1;
            }
            else if( regionType.equalsIgnoreCase(LEVEL_SHORT_2)) {
                fullName += " " + LEVEL_LONG_2;
            }
            else if( regionType.equalsIgnoreCase(LEVEL_SHORT_3)) {
                shortName = fullName;
                fullName += " " + LEVEL_LONG_3;
            }
            else if( regionType.equalsIgnoreCase(LEVEL_SHORT_4)) {
                fullName += " " + LEVEL_SHORT_4;
            }

            fullName = fullName.replaceAll("[-]", " ").trim();
            if( !addresses.contains(fullName))
                addresses.add(fullName);

            if( !Strings.isNullOrEmpty(shortName) ) {
                if( !addresses.contains(shortName))
                    addresses.add(shortName);
            }
        }


        logger.info("Found " + addresses.size() + " major locations");
    }

    public synchronized List<String> getAddresses() {
        return addresses;
    }
}
