package com.petrpopov.opennlprus.dao;

import com.petrpopov.opennlprus.entity.Address;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<Address> findAllCities() {

        String query = "select a from " + Address.class.getName() + " a where a.aolevel = :aolevel";
        List<Address> list = this.findByQuery(query, "aolevel", 4);

        return list;
    }
}
