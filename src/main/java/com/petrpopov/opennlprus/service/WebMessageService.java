package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.crawl.CrawlerManager;
import com.petrpopov.opennlprus.domain.dao.WebTextDao;
import com.petrpopov.opennlprus.domain.entity.WebText;
import com.petrpopov.opennlprus.dto.ParseMessage;
import com.petrpopov.opennlprus.dto.WebMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: petrpopov
 * Date: 01.12.13
 * Time: 11:47
 */

@Component
public class WebMessageService {


    @Value("${web_message_limit}")
    private Long WEB_MESSAGE_LIMIT;

    @Value("${check_web_message_limit}")
    private Boolean CHECK_WEB_MESSAGE_LIMIT;

    @Autowired
    private Splitter splitter;

    @Autowired
    private WebTextDao webTextDao;

    @Autowired
    private CrawlerManager crawlerManager;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(WebMessage webMessage) {

        List<ParseMessage> list = splitter.split(webMessage);
        for (ParseMessage message : list) {

            String text = message.getText();
            Integer count = webTextDao.countByText(text);
            if( count > 0 )
                continue;

            WebText webText = new WebText(message.getMessageUrl().getUrl(),
                    message.getMessageUrl().getNumber(), text);
            webTextDao.save(webText);
        }
    }

    public void stopIfNeed() {
        if( CHECK_WEB_MESSAGE_LIMIT.equals(Boolean.FALSE) )
            return;

        Long count = webTextDao.count();
        if( count >= WEB_MESSAGE_LIMIT )
            crawlerManager.stop();
    }
}
