package com.petrpopov.opennlprus.service;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by petrpopov on 25.12.13.
 */

@Component
public class SearchAnalyzer {

    @Autowired
    private AddressService addressService;

    @Autowired
    private SearchServerService searchServer;

    @Value("${search_max_count}")
    private Integer maxCount;

    @Value("${search_min_count}")
    private Integer minCount;

    @Value("${model_train_file}")
    private String modelTrainFile;

    @Value("${info_train_file}")
    private String infoTrainFile;

    private List<String> excludes = Arrays.asList("Того", "того", "Перу", "Порт-Вила", "Порт-Луи",
            "Порт-Морсби", "Порт-о-Пренс", "Порт-оф-Спейн", "Порто-Ново", "Порт", "порту");

    private Logger logger = Logger.getLogger(SearchAnalyzer.class);

    public void analyzeGeo() throws IOException {

        List<String> addresses = addressService.getAddresses();

        int num = 0;

        for (String address : addresses) {
            String q = address.trim();

            logger.info("Searching for adress: " + q);
            if( excludes.contains(q) ) {
                logger.info("Omitting " + q + " for searching");
                continue;
            }

            String query = getQueryForAddress(q);
            List<String> search = searchServer.search(query, minCount, maxCount);

            saveToModelFile(search, num);
            saveToInfoFile(q, search.size(), num);
            num++;
        }

    }

    private void saveToModelFile(List<String> strings, int num) throws IOException {

        if( strings.isEmpty() )
            return;

        if( num == 0 ) {
            FileUtils.writeLines(new File(modelTrainFile), strings);
        }
        else {
            FileUtils.writeLines(new File(modelTrainFile), strings, true);
        }

    }

    private void saveToInfoFile(String address, int count, int num) throws IOException {

        if( count == 0 )
            return;

        String str = address + "\t" + count + "\n";
        if( num == 0 ) {
            FileUtils.write(new File(infoTrainFile), str);
        }
        else {
            FileUtils.write(new File(infoTrainFile), str, true);
        }
    }

    private String getQueryForAddress(String address) {
        String q = "";
        String[] split = address.split(" ");
        if( split.length > 1 ) {

            for(int i = 0; i < split.length; i++) {
                q += split[i] + " ";
                if( i != split.length-1 ) {
                    q += "AND ";
                }
            }
        }
        else {
            q = address;
        }
        q = q.trim();

        return q;
    }

}
