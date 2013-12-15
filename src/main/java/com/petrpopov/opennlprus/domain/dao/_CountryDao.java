package com.petrpopov.opennlprus.domain.dao;

import com.petrpopov.opennlprus.domain.entity._Country;
import org.springframework.stereotype.Component;

/**
 * Created by petrpopov on 10.12.13.
 */

@Component
public class _CountryDao extends EntityDao<_Country> {

    public _CountryDao() {
        super(_Country.class);
    }
}
