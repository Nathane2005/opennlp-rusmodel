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
@Table(name="_countries")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "_countries")
public class _Country {

    @Id
    @Column(name = "country_id")
    private Long id;

    @Column(name = "title_ru", length = 50)
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        _Country country = (_Country) o;

        if (id != null ? !id.equals(country.id) : country.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
