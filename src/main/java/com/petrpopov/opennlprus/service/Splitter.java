package com.petrpopov.opennlprus.service;

import com.google.common.base.Strings;
import com.petrpopov.opennlprus.dto.ParseMessage;
import com.petrpopov.opennlprus.dto.WebMessage;
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

        String[] split = text.split("\\s{3,}");
        for (String s : split) {

            if(Strings.isNullOrEmpty(s) )
                continue;

            String work = s.trim();
            if( Strings.isNullOrEmpty(work))
                continue;

            work.replaceAll("\\s+", "");

            String[] strings = work.split("[.!?]");
            if( strings.length > 0 ) {

                for (String string : strings) {
                    String el = string.trim();
                    if( Strings.isNullOrEmpty(el) )
                        continue;

                    res.add(el);
                }
            }
            else {
                res.add(work);
            }
        }


        return res;
    }
}
