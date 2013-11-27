package com.petrpopov.opennlprus.dao;

import com.petrpopov.opennlprus.entity.Address;
import org.springframework.stereotype.Component;

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
}
