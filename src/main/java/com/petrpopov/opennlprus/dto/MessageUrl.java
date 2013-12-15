package com.petrpopov.opennlprus.dto;

import java.io.Serializable;

/**
 * User: petrpopov
 * Date: 30.11.13
 * Time: 23:44
 */
public class MessageUrl implements Serializable {

    private String url;
    private int number;

    public MessageUrl() {
    }

    public MessageUrl(String url, int number) {
        this.url = url;
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageUrl that = (MessageUrl) o;

        if (number != that.number) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + number;
        return result;
    }

    @Override
    public String toString() {
        return "MessageUrl: " + url + ", number: " +number;
    }
}
