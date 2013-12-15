package com.petrpopov.opennlprus.domain.entity;

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
@Table(name="_regions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "_regions")
public class _Region {

    @Id
    @Column(name = "region_id")
    private Long id;

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

        _Region region = (_Region) o;

        if (id != null ? !id.equals(region.id) : region.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
