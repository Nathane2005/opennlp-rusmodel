package com.petrpopov.opennlprus.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by petrpopov on 10.12.13.
 */

@Entity
@Table(name="_cities")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "_cities")
public class _City {

    @Id
    @Column(name = "city_id")
    private Long id;

    @Column(name = "region_id")
    private Long regionId;

    @Column(name = "country_id")
    private Long countryId;

    @Column(name = "title_ru", length = 100)
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        _City city = (_City) o;

        if (id != null ? !id.equals(city.id) : city.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
