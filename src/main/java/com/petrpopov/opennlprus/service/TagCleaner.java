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

    private static final String EMPTY_SPACE = "\\s{2,}";

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

        String clean = cleanAllButFirstTag(str, startTag);

        clean = cleanAllButLastTag(clean, endTag);
        clean = clean.replaceAll(EMPTY_SPACE, " ");
        clean = checkForEmptySpace(clean, startTag, true);
        clean = checkForEmptySpace(clean, endTag, false);
        clean = clean.trim();

        return clean;
    }

    private String cleanAllButFirstTag(String str, String tag) {

        int tagCount = count(str, tag);
        if( tagCount <= 1 )
            return str;

        String work = str;

        int first = work.indexOf(tag);
        first += 1;
        while(true) {
            int indexOf = work.indexOf(tag, first);
            if( indexOf == -1 )
                break;

            StringBuilder builder = new StringBuilder();
            builder.append(work);
            StringBuilder delete = builder.delete(indexOf, indexOf + tag.length());
            work = delete.toString();
        }

        return work;
    }

    private String cleanAllButLastTag(String str, String tag) {

        int tagCount = count(str, tag);
        if( tagCount <= 1 )
            return str;

        String work = str;

        for(int i = 0; i < tagCount-1; i++) {

            int indexOf = work.indexOf(tag);
            if( indexOf == -1 )
                break;

            StringBuilder builder = new StringBuilder();
            builder.append(work);
            StringBuilder delete = builder.delete(indexOf, indexOf + tag.length());
            work = delete.toString();
        }

        return work;
    }

    private String checkForEmptySpace(String str, String tag, boolean start) {

        int indexOf = str.indexOf(tag);
        if( indexOf == -1 )
            return str;

        String work = str;
        if( start ) {
            indexOf--;
            if( indexOf < 0 )
                return str;

            char c = str.charAt(indexOf);
            if( c == ' ' )
                return str;

            work = str.replace(tag, " " + tag);
        }
        else {
            indexOf = tag.length() + 1;
            if( indexOf >= str.length() )
                return str;

            char c = str.charAt(indexOf);
            if( c == ' ' )
                return str;

            work = str.replace(tag, tag + " ");
        }

        return work;
    }

    private int count(String str, String pattern) {

        int count = 0;
        int index = 0;
        while (true) {
            int found = str.indexOf(pattern, index);
            if( found == -1 )
                break;

            count++;
            index = found+pattern.length();
        }

        return count;
    }
}
