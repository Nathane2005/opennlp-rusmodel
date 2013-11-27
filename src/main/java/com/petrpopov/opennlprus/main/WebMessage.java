package com.petrpopov.opennlprus.main;

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

    private String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    private String getText() {
        return text;
    }

    private void setText(String text) {
        this.text = text;
    }
}
