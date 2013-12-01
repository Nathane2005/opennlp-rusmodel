package com.petrpopov.opennlprus.dao;

import com.petrpopov.opennlprus.entity.WebText;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public Integer countByText(String text) {

        String query = "select w from " + WebText.class.getName() + " w where w.text=:text";

        List<WebText> list = this.findByQuery(query, "text", text);
        if( list == null )
            return 0;

        return list.size();
    }
}
