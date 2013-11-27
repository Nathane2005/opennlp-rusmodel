package com.petrpopov.opennlprus.main;

import org.apache.activemq.command.ActiveMQMessage;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * User: petrpopov
 * Date: 28.11.13
 * Time: 1:26
 */

@Component
public class MessageReciever implements SessionAwareMessageListener {


    @Override
    public void onMessage(Message message, Session session) throws JMSException {

        ActiveMQMessage mes = (ActiveMQMessage) message;

        String url = message.getStringProperty("url");
        String text = message.getStringProperty("text");

        System.out.println("Message recieved: " + url);
    }
}
