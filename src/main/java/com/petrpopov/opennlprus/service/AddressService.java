package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.domain.dao.*;
import com.petrpopov.opennlprus.domain.entity.GeoName;
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
    private final String LEVEL_SHORT_5 = "р-н";

    private final String LEVEL_LONG_2 = "область";
    private final String LEVEL_LONG_3 = "республика";
    private final String LEVEL_LONG_5 = "район";


    @Autowired
    private AddressDao addressDao;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private _CityDao cityDao;

    @Autowired
    private _CountryDao _countryDao;

    @Autowired
    private _RegionDao regionDao;

    @Autowired
    private GeoNameDao geoNameDao;


    private volatile List<String> addresses = new ArrayList<String>();

    private Logger logger = Logger.getLogger(AddressService.class);

    @PostConstruct
    public void init() {

        logger.info("Finding all major locations...");

        List<GeoName> all = geoNameDao.findAll();
        for (GeoName geoName : all) {
            addresses.add(geoName.getTitle());
        }


        logger.info("FOUND " + addresses.size() + " TOTAL LOCATIONS");

        /*
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
            else if( regionType.equalsIgnoreCase(LEVEL_SHORT_5)) {
                shortName = fullName;
                fullName += " " + LEVEL_LONG_5;
            }

            fullName = fullName.replaceAll("[-]", " ").trim();
            if( !addresses.contains(fullName)) {
                if( !excludes.contains(fullName)) {
                    addresses.add(fullName);
                }
            }

            if( !Strings.isNullOrEmpty(shortName) ) {
                if( !addresses.contains(shortName))  {
                    if( !excludes.contains(shortName)) {
                        addresses.add(shortName);
                    }
                }
            }
        }


        List<Country> countryList = countryDao.findAll();
        for (Country country : countryList) {

            String countryName = country.getCountryName();

            if( !addresses.contains(countryName) ) {
                if( !excludes.contains(countryName)) {
                    addresses.add(countryName);
                }
            }

        }


        logger.info("Found " + addresses.size() + " major locations");


        logger.info("Finding all countries...");
        List<_Country> countries = _countryDao.findAll();
        logger.info("Found " + countries.size() + " countries");

        for (_Country country : countries) {

            String title = country.getTitle();

            if( !addresses.contains(title)) {
                if( !excludes.contains(title) ) {
                    addresses.add(title);
                }
            }

        }


        logger.info("Finding all regions...");
        List<_Region> regions = regionDao.findAll();
        logger.info("Found " + regions.size() + " regions");

        for (_Region region : regions) {

            String title = region.getTitle();

            if( !addresses.contains(title)) {
                if( !excludes.contains(title)) {
                    addresses.add(title);
                }
            }
        }


        logger.info("Finding all cities...");
        List<_City> cities = cityDao.findAll();
        logger.info("Found " + cities.size() + " cities");

        for (_City city : cities) {

            String title = city.getTitle();
            if( !addresses.contains(title)) {
                if( !excludes.contains(title)) {
                    addresses.add(title);
                }
            }
        }
              */


    }

    public synchronized List<String> getAddresses() {
        return addresses;
    }
}
