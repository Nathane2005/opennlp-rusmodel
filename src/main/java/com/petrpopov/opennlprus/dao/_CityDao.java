package com.petrpopov.opennlprus.dao;

import com.petrpopov.opennlprus.entity._City;
import org.springframework.stereotype.Component;

/**
 * Created by petrpopov on 10.12.13.
 */

@Component
public class _CityDao extends EntityDao<_City> {

    public _CityDao() {
        super(_City.class);
    }
}
