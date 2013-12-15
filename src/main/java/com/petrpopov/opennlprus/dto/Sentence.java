package com.petrpopov.opennlprus.dto;

import java.io.Serializable;

/**
 * Created by petrpopov on 15.12.13.
 */
public class Sentence implements Serializable {

    private String url;
    private Integer number;
    private String value;

    public Sentence(String url, Integer number, String value) {
        this.url = url;
        this.number = number;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sentence sentence = (Sentence) o;

        if (value != null ? !value.equals(sentence.value) : sentence.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
