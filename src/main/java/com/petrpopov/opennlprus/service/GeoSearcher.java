package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.domain.dao.GeoWebTextDao;
import com.petrpopov.opennlprus.domain.dao.WebTextDao;
import com.petrpopov.opennlprus.domain.entity.GeoWebText;
import com.petrpopov.opennlprus.domain.entity.WebText;
import com.petrpopov.opennlprus.dto.ParseMessage;
import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: petrpopov
 * Date: 29.11.13
 * Time: 0:35
 */

@Component
public class GeoSearcher {

    @Value("${string_parse_max_length}")
    private Integer STRING_PARSE_MAX_LENGTH;

    @Autowired
    private AddressService addressService;

    @Autowired
    private LuceneService luceneService;

    @Autowired
    private GeoWebTextDao geoWebTextDao;

    @Autowired
    private WebTextDao webTextDao;

    private  List<String> excludes = Arrays.asList("Донской", "Coupé", "Divišov", "Odón", "Pathé"
    ,"Абу", "Абана", "Абаев", "Аббас", "Абдуллах", "Абдулла", "Абдулово", "Абрамов", "Абдуллу");

    private Logger logger = Logger.getLogger(GeoSearcher.class);

    public void buildIndex() throws IOException {

        logger.info("Preparing the big fucking index");

        List<WebText> list = webTextDao.findAll();
        for (WebText webText : list) {
            if(webText.getText().length() >= STRING_PARSE_MAX_LENGTH )
                continue;
            if( webText.getText().contains("\n"))
                continue;

            luceneService.addDocument(webText.getUrl(), webText.getNumber(), webText.getText());
        }

        logger.info("End of preparing index");
    }

    public void uberFuckingMethod() throws IOException, InvalidTokenOffsetsException {

        int i = 1;
        List<String> addresses = addressService.getAddresses();
        for (String address : addresses) {
            logger.info(i + ":" + addresses.size() + " Searching for address: " + address);

            String q = address.trim();
            if( excludes.contains(q) )
                continue;

            List<ParseMessage> geo = luceneService.searchGeo(q);
            for (ParseMessage message : geo) {

                boolean ok = true;
                String text = message.getText();
                for (String exclude : excludes) {
                    if( text.contains(exclude) ) {
                        ok = false;
                        break;
                    }
                }

                if( !ok )
                    continue;

                String url = message.getMessageUrl().getUrl();
                if( url.contains("sport"))
                    continue;

                int length = 0;
                int i1 = text.indexOf("<START");
                if( i1 >= 0 ) {
                    int i2 = text.indexOf("END>");
                    if( i2 >= 0 ) {
                        String cut = text.substring(0, i1) + text.substring(i2+1+3);
                        length = cut.length();
                    }
                }

                if( length < 10 ) {
                    continue;
                }


                GeoWebText geoWebText = new GeoWebText();

                geoWebText.setUrl(url);
                geoWebText.setNumber(message.getMessageUrl().getNumber());
                geoWebText.setText(text);
                geoWebText.setOriginalText(message.getOriginalText());

                geoWebTextDao.save(geoWebText);
            }


            i++;
        }

    }

    public synchronized int containsGeoName(ParseMessage message) throws IOException, ParseException {

        logger.info("Searching for geo locations in: " + message);
        return containsGeoName(message.getMessageUrl().getUrl(), message.getText());
    }

    public synchronized int containsGeoName(String url, String text) throws IOException, ParseException {

        int count = 0;
        List<String> found = new ArrayList<String>();

        for (String address : addressService.getAddresses()) {

            logger.debug("Using address for search: " + address);

            List<String> list = luceneService.search(address, "text");
            if( list.isEmpty() )
                continue;

            for (String url1 : list) {
                if( url1.equals(url) ) {
                    found.add(address);
                    count++;
                    break;
                }
            }
        }

        if( count > 0 )
            logger.info("FOUND!!!");

        return count;
    }
}
