package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.other.ParseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * User: petrpopov
 * Date: 30.11.13
 * Time: 23:14
 */

@Component
public class MessageKeeper {

    @Autowired
    private CacheManager cacheManager;

    public synchronized void add(ParseMessage message) {

        if(contains(message))
            return;

        getCache().put(message.getText(), message);
    }

    public synchronized boolean contains(ParseMessage message) {

        Cache.ValueWrapper wrapper = getCache().get(message.getText());
        if( wrapper == null )
            return false;
        return true;
    }

    private Cache getCache() {
        Cache cache = cacheManager.getCache("sentences");
        return cache;
    }
}
