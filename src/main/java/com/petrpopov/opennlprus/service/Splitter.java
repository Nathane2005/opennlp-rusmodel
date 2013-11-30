package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.other.ParseMessage;
import com.petrpopov.opennlprus.other.WebMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: petrpopov
 * Date: 30.11.13
 * Time: 22:52
 */

@Component
public class Splitter {

    public List<ParseMessage> split(WebMessage message) {

        List<ParseMessage> res = new ArrayList<ParseMessage>();

        List<String> list = split(message.getText());
        for(int i = 0; i < list.size(); i++) {
            String str = list.get(i);

            ParseMessage mes = new ParseMessage(message.getUrl(), i, str);
            res.add(mes);
        }


        return res;
    }

    public List<String> split(String text) {

        List<String> res = new ArrayList<String>();

        String[] split = text.split("\\s{2,}");
        for (String s : split) {

            String[] strings = s.split(".");

            if( strings.length > 0 ) {
                for (String string : strings) {
                    res.add(string);
                }
            }
            else {
                res.add(s);
            }
        }


        return res;
    }
}
