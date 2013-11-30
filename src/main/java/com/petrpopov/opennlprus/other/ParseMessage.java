package com.petrpopov.opennlprus.other;

import java.io.Serializable;

/**
 * User: petrpopov
 * Date: 30.11.13
 * Time: 23:15
 */

public class ParseMessage implements Serializable {

    private String text;
    private MessageUrl messageUrl;

    public ParseMessage() {
    }

    public ParseMessage(String url, int number, String text) {
        MessageUrl messageUrl1 = new MessageUrl(url, number);
        this.messageUrl = messageUrl1;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public MessageUrl getMessageUrl() {
        return messageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParseMessage that = (ParseMessage) o;

        if (messageUrl != null ? !messageUrl.equals(that.messageUrl) : that.messageUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return messageUrl != null ? messageUrl.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ParseMessage: " + messageUrl;
    }
}
