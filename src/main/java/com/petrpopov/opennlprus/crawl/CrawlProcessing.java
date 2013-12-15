package com.petrpopov.opennlprus.crawl;

import com.petrpopov.opennlprus.dto.Sentence;
import com.petrpopov.opennlprus.dto.WebMessage;
import com.petrpopov.opennlprus.service.Tokenizer;
import com.petrpopov.opennlprus.service.WebMessageService;
import com.petrpopov.opennlprus.support.OpException;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petrpopov on 15.12.13.
 */

@Component
public class CrawlProcessing {

    @Autowired
    private JmsTemplate template;

    @Autowired
    private Tokenizer tokenizer;

    @Autowired
    private WebMessageService webMessageService;

    public void proccessMessage(WebMessage message) throws OpException {

        String text;
        try {
            text = ArticleExtractor.INSTANCE.getText(message.getHtml());
        } catch (BoilerpipeProcessingException e) {
            throw new OpException(e);
        }


        List<Sentence> sentences = getSentences(text, message.getUrl());
        save(sentences);
    }

    private void save(List<Sentence> sentences) {

        for (Sentence sentence : sentences) {

            webMessageService.save(sentence);
        }
    }

    private List<Sentence> getSentences(String text, String url) {

        List<String> strings = tokenizer.tokenize(text);
        List<Sentence> sentences = new ArrayList<Sentence>();

        int count = 0;
        for (String string : strings) {
            Sentence sentence = new Sentence(url, count, string);
            sentences.add(sentence);
            count++;
        }

        return sentences;
    }

    //send message to ActiveMQ
    public void sendMessage(final WebMessage message) {
        template.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(message);
            }
        });
    }
}
