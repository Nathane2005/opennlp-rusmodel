package com.petrpopov.opennlprus.domain.dao;

import com.petrpopov.opennlprus.domain.entity.Country;
import org.springframework.stereotype.Component;

/**
 * User: petrpopov
 * Date: 01.12.13
 * Time: 16:36
 */

@Component
public class CountryDao extends EntityDao<Country> {

    public CountryDao() {
        super(Country.class);
    }
}
