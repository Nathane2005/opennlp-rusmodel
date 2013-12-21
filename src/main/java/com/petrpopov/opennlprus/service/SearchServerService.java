package com.petrpopov.opennlprus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petrpopov.opennlprus.dto.Sentence;
import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
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

    @Value("${search_type1}")
    private String searchType1;

    @Value("${search_type2}")
    private String searchType2;


    private volatile Client client;
    private volatile ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = Logger.getLogger(SearchServerService.class);

    @PostConstruct
    public void init() {
        client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(host, port));
    }


    public synchronized void search(String q) {

        SearchResponse response = client.prepareSearch(searchIndex)
                .setTypes(searchType1)
                .setQuery(QueryBuilders.fieldQuery("body", q))             // Query
                .execute()
                .actionGet();

        logger.info(response);
    }

    public synchronized void save(Sentence sentence) throws JsonProcessingException {

        String json = mapper.writeValueAsString(sentence);

        IndexResponse response = client.prepareIndex(searchIndex, searchType1)
                .setSource(json)
                .execute()
                .actionGet();

        String id1 = response.getId();

        response = client.prepareIndex(searchIndex, searchType2)
                .setSource(json)
                .execute()
                .actionGet();

        String id2 = response.getId();

        logger.info("Saved document with id1: " +id1 + " and id2: " + id2);
    }

    public synchronized void save(List<Sentence> sentences) throws JsonProcessingException {

       /* BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (Sentence sentence : sentences) {

            String json = mapper.writeValueAsString(sentence);
            IndexRequestBuilder source1 = client.prepareIndex(searchIndex, searchType1)
                    .setSource(json);

            bulkRequest.add(source1);

            IndexRequestBuilder source2 = client.prepareIndex(searchIndex, searchType2)
                    .setSource(json);

            bulkRequest.add(source2);
        }

        BulkResponse response = bulkRequest.execute().actionGet();
        logger.info("Saved documents"); */

        for (Sentence sentence : sentences) {
            save(sentence);
        }

    }

}
