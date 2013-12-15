package com.petrpopov.opennlprus.domain.dao;

import com.petrpopov.opennlprus.domain.entity.GeoWebText;
import org.springframework.stereotype.Component;

/**
 * User: petrpopov
 * Date: 01.12.13
 * Time: 19:14
 */

@Component
public class GeoWebTextDao extends EntityDao<GeoWebText> {

    public GeoWebTextDao() {
        super(GeoWebText.class);
    }
}
