package com.petrpopov.opennlprus.domain.dao;

import com.petrpopov.opennlprus.domain.entity.Address;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: petrpopov
 * Date: 27.11.13
 * Time: 22:58
 */

@Component
public class AddressDao extends EntityDao<Address> {

    public AddressDao() {
        super(Address.class);
    }

    public List<Address> findAllMajor() {

        String query = "select a from " + Address.class.getName() +
                " a where a.aolevel = :aolevel1 or a.aolevel = :aolevel2 or a.aolevel = :aolevel3";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("aolevel1", 1);
        params.put("aolevel2", 4);
        params.put("aolevel3", 3);

        List<Address> list = this.findByQuery(query, params);

        return list;
    }
}
