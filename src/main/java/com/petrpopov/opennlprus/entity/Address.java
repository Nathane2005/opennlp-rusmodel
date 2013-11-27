package com.petrpopov.opennlprus.entity;

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
public class Address {

    @Id
    @Column(name = "aoid", columnDefinition = "char", length = 36)
    private String aoid;

    @Column(name = "formalname", length = 120)
    private String formalname;

    @Column(name = "aolevel")
    private Integer aolevel;

    public String getAoid() {
        return aoid;
    }

    public void setAoid(String aoid) {
        this.aoid = aoid;
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

    @Override
    public String toString() {
        return "Address: " + formalname + " type: " + aolevel;
    }
}
