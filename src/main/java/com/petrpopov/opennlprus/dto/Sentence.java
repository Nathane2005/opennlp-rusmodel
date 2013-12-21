package com.petrpopov.opennlprus.dto;

import java.io.Serializable;

/**
 * Created by petrpopov on 15.12.13.
 */
public class Sentence implements Serializable {

    private String url;
    private Integer number;
    private String body;

    public Sentence(String url, Integer number, String body) {
        this.url = url;
        this.number = number;
        this.body = body;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sentence sentence = (Sentence) o;

        if (body != null ? !body.equals(sentence.body) : sentence.body != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return body != null ? body.hashCode() : 0;
    }
}
