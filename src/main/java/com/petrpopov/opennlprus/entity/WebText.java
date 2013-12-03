package com.petrpopov.opennlprus.entity;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * User: petrpopov
 * Date: 28.11.13
 * Time: 0:18
 */

@Entity
@Table(name = "web_text")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "web_text")
public class WebText {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "number")
    private Integer number;

    @Column(name = "text", columnDefinition = "text", unique = true, length = 1000)
    private String text;

    public WebText() {
    }

    public WebText(String url, Integer number, String text) {
        this.url = url;
        this.number = number;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebText that = (WebText) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
