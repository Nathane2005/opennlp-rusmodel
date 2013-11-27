package com.petrpopov.opennlprus.dao;

import com.petrpopov.opennlprus.entity.WebText;
import org.springframework.stereotype.Component;

/**
 * User: petrpopov
 * Date: 28.11.13
 * Time: 0:24
 */

@Component
public class WebTextDao extends EntityDao<WebText> {

    public WebTextDao() {
        super(WebText.class);
    }
}
