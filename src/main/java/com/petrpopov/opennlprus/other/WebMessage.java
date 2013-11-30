package com.petrpopov.opennlprus.other;

import java.io.Serializable;

/**
 * User: petrpopov
 * Date: 28.11.13
 * Time: 1:28
 */
public class WebMessage implements Serializable {

    private String url;
    private String text;

    public WebMessage() {
    }

    public WebMessage(String url, String text) {
        this.url = url;
        this.text = text;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

        WebMessage that = (WebMessage) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WebMessage: " + url;
    }
}
