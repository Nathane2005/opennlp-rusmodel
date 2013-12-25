package com.petrpopov.opennlprus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petrpopov.opennlprus.dto.Sentence;
import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Value("${start_tag}")
    private String startTag;

    @Value("${end_tag}")
    private String endTag;

    @Autowired
    private TagCleaner tagCleaner;

    private volatile Client client;
    private volatile ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = Logger.getLogger(SearchServerService.class);

    @PostConstruct
    public void init() {
        client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(host, port));
    }


    public long search(String q) {

        SearchResponse response = searchResponse(q);

        //logger.info(response);
        //Map<String,HighlightField> fields = response.getHits().getAt(0).highlightFields();

        SearchHits hits = response.getHits();
        return hits.totalHits();
    }

    public List<String> search(String q, Integer count) {

        List<String> res = new ArrayList<String>();

        SearchResponse response = searchResponse(q);
        SearchHits hits = response.getHits();

        int counter = 0;
        for (SearchHit hit : hits) {
            if( counter == count )
                break;

            Map<String, HighlightField> fields = hit.highlightFields();
            for (Map.Entry<String, HighlightField> entry : fields.entrySet()) {
                HighlightField field = entry.getValue();
                Text[] fragments = field.getFragments();
                for (Text fragment : fragments) {
                    res.add(fragment.string());
                }

            }


            counter++;
        }

        List<String> clean = tagCleaner.cleanTag(res);

        return clean;
    }


    private synchronized SearchResponse searchResponse(String q) {
        SearchResponse response = client.prepareSearch(searchIndex)
                .setTypes(searchType1)
                .setQuery(QueryBuilders.fieldQuery("body", q))             // Query
                .addHighlightedField("body", 1000, 1000)
                .setHighlighterPreTags(" " + startTag + " ")
                .setHighlighterPostTags(" " + endTag + " ")
                .execute()
                .actionGet();

        return response;
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
