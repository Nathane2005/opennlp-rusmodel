package com.petrpopov.opennlprus.domain.dao;

import com.petrpopov.opennlprus.domain.entity.GeoName;
import org.springframework.stereotype.Component;

/**
 * Created by petrpopov on 12.12.13.
 */

@Component
public class GeoNameDao extends EntityDao<GeoName> {

    public GeoNameDao() {
        super(GeoName.class);
    }
}
