package com.petrpopov.opennlprus.service;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * User: petrpopov
 * Date: 14.12.13
 * Time: 17:39
 */

public class Tokenizer {

    public static List<String> tokenize(String text) {

        List<String> res = new ArrayList<String>();

        if(Strings.isNullOrEmpty(text) ) {
            return res;
        }

        String trim = text.trim();
        String[] enter = trim.split("\n");

        for (String string : enter) {

            String s0 = string.trim();
            if( Strings.isNullOrEmpty(s0) )
                continue;

            String[] split2 = s0.split("\t");
            for (String s : split2) {

                String[] split3 = s.split("\\s{3,}");
                for (String ss : split3) {

                    String[] split = ss.split("[?!]");
                    for (String s1 : split) {

                        String s2 = s1.trim();
                        if(Strings.isNullOrEmpty(s2))
                            continue;

                        String[] split1 = s2.split("\\.[^A-Za-z0-9]");
                        for (String s3 : split1){

                            String s4 = s3.trim();
                            if(Strings.isNullOrEmpty(s4))
                                continue;

                            res.add(s4);
                        }
                    }
                }
            }
        }

        res = clean(res);
        return res;
    }

    private static List<String> clean(List<String> strings) {

        List<String> res = new ArrayList<String>();
        for (String string : strings) {
            String clean = clean(string);
            res.add(clean);
        }

        return res;
    }

    private static String clean(String str) {

        String replace = str.replaceAll("([<>\\-_]+)$", "");
        replace.replaceAll("^([<>\\-_]+)","");
        replace = replace.trim();

        return replace;
    }
}
