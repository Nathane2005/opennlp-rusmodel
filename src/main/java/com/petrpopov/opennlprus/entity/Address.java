package com.petrpopov.opennlprus.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: petrpopov
 * Date: 27.11.13
 * Time: 22:49
 */

@Entity
@Table(name="addrobj")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "address")
public class Address {

    @Id
    @Column(name = "aoid", columnDefinition = "char", length = 36)
    private String id;

    @Column(name = "formalname", length = 120)
    private String formalname;

    @Column(name = "shortname", length = 10)
    private String shortname;

    @Column(name = "aolevel")
    private Integer aolevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormalname() {
        return formalname;
    }

    public void setFormalname(String formalname) {
        this.formalname = formalname;
    }

    public Integer getAolevel() {
        return aolevel;
    }

    public void setAolevel(Integer aolevel) {
        this.aolevel = aolevel;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    @Override
    public String toString() {
        return "Address: \"" + formalname + "\", type: " + aolevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (id != null ? !id.equals(address.id) : address.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
