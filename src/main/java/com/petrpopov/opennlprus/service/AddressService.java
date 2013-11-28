package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.dao.AddressDao;
import com.petrpopov.opennlprus.entity.Address;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * User: petrpopov
 * Date: 29.11.13
 * Time: 0:40
 */

@Component
public class AddressService {

    @Autowired
    private AddressDao addressDao;

    private volatile List<Address> addresses;

    private Logger logger = Logger.getLogger(AddressService.class);

    @PostConstruct
    public void init() {

        logger.info("Location all cities...");

        addresses = addressDao.findAllCities();

        logger.info("Found " + addresses.size() + " cities.");
    }

    public synchronized List<Address> getAddresses() {
        return addresses;
    }
}
