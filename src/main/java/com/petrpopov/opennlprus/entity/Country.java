package com.petrpopov.opennlprus.entity;

import javax.persistence.*;

/**
 * User: petrpopov
 * Date: 01.12.13
 * Time: 16:33
 */

@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_name", unique = true)
    private String countryName;

    public Country() {
    }

    public Country(String countryName) {
        this.countryName = countryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
