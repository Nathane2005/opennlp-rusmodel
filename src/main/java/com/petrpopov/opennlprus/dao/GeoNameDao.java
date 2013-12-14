package com.petrpopov.opennlprus.dao;

import com.petrpopov.opennlprus.entity.GeoName;
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
