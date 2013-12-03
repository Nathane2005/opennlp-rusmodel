package com.petrpopov.opennlprus.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * User: petrpopov
 * Date: 01.12.13
 * Time: 19:13
 */

@Entity
@Table(name = "geo_web_text")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "geo_web_text")
public class GeoWebText {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "number")
    private Integer number;

    @Column(name = "text", columnDefinition = "text")
    private String text;

    public GeoWebText() {
    }

    public GeoWebText(String url, Integer number, String text) {
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
}
