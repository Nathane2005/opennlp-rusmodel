package com.petrpopov.opennlprus.dao;

import com.petrpopov.opennlprus.entity._Region;
import org.springframework.stereotype.Component;

/**
 * Created by petrpopov on 10.12.13.
 */

@Component
public class _RegionDao extends EntityDao<_Region> {

    public _RegionDao() {
        super(_Region.class);
    }
}
