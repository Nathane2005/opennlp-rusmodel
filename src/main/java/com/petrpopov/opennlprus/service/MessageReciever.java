package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.other.WebMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;

/**
 * User: petrpopov
 * Date: 28.11.13
 * Time: 1:26
 */

@Component
public class MessageReciever implements SessionAwareMessageListener {

    @Autowired
    private LuceneService luceneService;

    private Logger logger = Logger.getLogger(MessageReciever.class);

    @Override
    public void onMessage(Message message, Session session) throws JMSException {

        logger.info("Message recieved.");

        if( !(message instanceof ObjectMessage) )
            return;

        ObjectMessage mes = (ObjectMessage) message;

        Serializable object = mes.getObject();
        if( object == null )
            return;

        if( !(object instanceof WebMessage) )
            return;

        WebMessage webMessage = (WebMessage) object;

        try {
            boolean contains = luceneService.contains(webMessage);

            if( !contains ) {
                logger.info("Object is not in the index. Adding.");

                luceneService.addDocument(webMessage);
            }
            else {
                logger.info("Object is in the index. Skipping.");
                return;
            }
        } catch (Exception e) {

        }

        String url = message.getStringProperty("url");
        String text = message.getStringProperty("text");

        System.out.println("Message recieved: " + url);
    }
}