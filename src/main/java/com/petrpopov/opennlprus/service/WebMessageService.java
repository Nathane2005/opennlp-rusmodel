package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.crawl.CrawlerManager;
import com.petrpopov.opennlprus.domain.dao.WebTextDao;
import com.petrpopov.opennlprus.domain.entity.WebText;
import com.petrpopov.opennlprus.dto.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private WebTextDao webTextDao;

    @Autowired
    private CrawlerManager crawlerManager;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(Sentence sentence) {

        Integer count = webTextDao.countByText(sentence.getValue());
        if( count > 0 )
            return;

        WebText webText = new WebText(sentence.getUrl(), sentence.getNumber(), sentence.getValue());
        webTextDao.save(webText);
    }

    public void stopIfNeed() {
        if( CHECK_WEB_MESSAGE_LIMIT.equals(Boolean.FALSE) )
            return;

        Long count = webTextDao.count();
        if( count >= WEB_MESSAGE_LIMIT )
            crawlerManager.stop();
    }
}
