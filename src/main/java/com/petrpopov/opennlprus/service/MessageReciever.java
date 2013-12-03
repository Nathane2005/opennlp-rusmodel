package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.other.WebMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private WebMessageAnalyzer webMessageAnalyzer;

    @Autowired
    private WebMessageService webMessageService;

    @Autowired
    private CrawlerManager crawlerManager;

    private Logger logger = Logger.getLogger(MessageReciever.class);

    private volatile long messageNumber = 0;

    @Value("${links_limit}")
    private Long linksLimit;

    @Override
    public void onMessage(Message message, Session session) throws JMSException {

        if( !(message instanceof ObjectMessage) )
            return;

        ObjectMessage mes = (ObjectMessage) message;

        Serializable object = mes.getObject();
        if( !(object instanceof WebMessage) )
            return;

        increment();

        if( getMessageNumber() >= linksLimit ) {
            logger.info("Enough crawling !");
            crawlerManager.stop();
            return;
        }

        WebMessage webMessage = (WebMessage) object;
        logger.info("Message number " + getMessageNumber()+ " recieved: " + webMessage.getUrl());
        if( crawlerManager.isStopped() ) {
            logger.info("Enough messages, skip");
            return;
        }

        //webMessageAnalyzer.analyze(webMessage);
        webMessageService.save(webMessage);
        webMessageService.stopIfNeed();
    }

    private synchronized void increment() {
        messageNumber++;
    }

    private synchronized long getMessageNumber() {
        return messageNumber;
    }
}
