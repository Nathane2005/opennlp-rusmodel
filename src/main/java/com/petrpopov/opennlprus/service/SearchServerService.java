package com.petrpopov.opennlprus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petrpopov.opennlprus.dto.Sentence;
import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by petrpopov on 15.12.13.
 */

@Component
public class SearchServerService {

    @Value("${search_host}")
    private String host;

    @Value("${search_port}")
    private Integer port;

    @Value("${search_index}")
    private String searchIndex;

    @Value("${search_type}")
    private String searchType;


    private volatile Client client;
    private volatile ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = Logger.getLogger(SearchServerService.class);

    @PostConstruct
    public void init() {
        client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(host, port));
    }

    public void save(Sentence sentence) throws JsonProcessingException {

        String json = mapper.writeValueAsString(sentence);

        IndexResponse response = client.prepareIndex(searchIndex, searchType)
                .setSource(json)
                .execute()
                .actionGet();

        String id = response.getId();
        logger.info("Saved document with id: " +id);
    }

    public void save(List<Sentence> sentences) throws JsonProcessingException {

        for (Sentence sentence : sentences) {
            save(sentence);
        }
    }

}
