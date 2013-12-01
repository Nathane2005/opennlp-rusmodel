package com.petrpopov.opennlprus.dao;

import com.petrpopov.opennlprus.entity.Address;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AddressDao extends EntityDao<Address> {

    public AddressDao() {
        super(Address.class);
    }

    public List<Address> findAllMajor() {

        String query = "select a from " + Address.class.getName() +
                " a where a.aolevel = :aolevel1 or a.aolevel = :aolevel2";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("aolevel1", 1);
        params.put("aolevel2", 4);

        List<Address> list = this.findByQuery(query, params);

        return list;
    }
}
