package com.petrpopov.opennlprus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petrpopov on 25.12.13.
 */

@Component
public class TagCleaner {

    @Value("${start_tag}")
    private String startTag;

    @Value("${end_tag}")
    private String endTag;


    public List<String> cleanTag(List<String> strs) {

        List<String> res = new ArrayList<String>();

        for (String str : strs) {
            String cleanTag = cleanTag(str);
            res.add(cleanTag);
        }


        return res;
    }


    public String cleanTag(String str) {

        int startCount = count(str, startTag);
        if( startCount <= 1 )
            return str;

        return str;
    }

    private int count(String str, String pattern) {

        int count = 0;
        int index = 0;
        while (true) {
            int found = str.indexOf(pattern, index);
            if( found == -1 )
                break;

            count++;
            index += found+pattern.length();
        }

        return count;
    }
}
