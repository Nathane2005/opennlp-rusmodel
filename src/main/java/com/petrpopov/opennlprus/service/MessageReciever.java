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

    @Autowired
    private GeoSearcher geoSearcher;

    private Logger logger = Logger.getLogger(MessageReciever.class);

    @Override
    public void onMessage(Message message, Session session) throws JMSException {

        if( !(message instanceof ObjectMessage) )
            return;

        ObjectMessage mes = (ObjectMessage) message;

        Serializable object = mes.getObject();
        if( !(object instanceof WebMessage) )
            return;

        WebMessage webMessage = (WebMessage) object;
        logger.info("Message recieved: " + webMessage.getUrl());

        try {
            boolean contains = luceneService.containsUrl(webMessage);

            if( !contains ) {
                logger.info("Object is not in the index. Adding");

                luceneService.addDocument(webMessage);

                logger.info("Looking for geo locations in message");
                int count = geoSearcher.containsGeoName(webMessage);

                if(count == 0)
                    return;

                logger.info("Found " + count + " geo locations in message " + webMessage.getUrl() );
            }
            else {
                logger.info("Object is in the index. Skipping");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
