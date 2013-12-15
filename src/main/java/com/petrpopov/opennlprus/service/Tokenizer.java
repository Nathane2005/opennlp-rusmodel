package com.petrpopov.opennlprus.service;

import com.google.common.base.Strings;
import com.petrpopov.opennlprus.support.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: petrpopov
 * Date: 14.12.13
 * Time: 17:39
 */

@Component
public class Tokenizer {

    @Value("${tokenizer_skip_length}")
    private Integer LENGTH;

    private static final String ENTER = "\n";
    private static final String TAB = "\t";
    private static final String EMPTY_SPACE = "\\s{3,}";
    private static final String DELIMETER = "[?!]";
    private static final String POINT_EXCEPT_NUMBERS = "\\.[^A-Za-z0-9]";
    private static final String LEFT_GARBAGE = "([<>\\-_]+)$";
    private static final String RIGHT_GARBAGE = "^([<>\\-_]+)";

    @Autowired
    private DateUtil dateUtil;

    public List<String> tokenize(String text) {

        List<String> res = new ArrayList<String>();

        if(Strings.isNullOrEmpty(text) ) {
            return res;
        }

        String trim = text.trim();
        String[] enter = trim.split(ENTER);

        for (String string : enter) {

            String s0 = string.trim();
            if( Strings.isNullOrEmpty(s0) )
                continue;

            String[] split2 = s0.split(TAB);
            for (String s : split2) {

                String[] split3 = s.split(EMPTY_SPACE);
                for (String ss : split3) {

                    String[] split = ss.split(DELIMETER);
                    for (String s1 : split) {

                        String s2 = s1.trim();
                        if(Strings.isNullOrEmpty(s2))
                            continue;

                        String[] split1 = s2.split(POINT_EXCEPT_NUMBERS);
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

    private List<String> clean(List<String> strings) {

        List<String> res = new ArrayList<String>();
        for (String string : strings) {

            String clean = clean(string);
            boolean isDate = isDateString(clean);
            if( isDate )
                continue;

            boolean isUrl = isURLString(clean);
            if( isUrl )
                continue;

            if( clean.length() <= LENGTH )
                continue;

            res.add(clean);
        }

        return res;
    }

    private String clean(String str) {

        String replace = str.replaceAll(LEFT_GARBAGE, "");
        replace.replaceAll(RIGHT_GARBAGE,"");
        replace = replace.trim();

        return replace;
    }

    private boolean isDateString(String str) {
        return dateUtil.isDateString(str);
    }

    private boolean isURLString(String str) {
        try {
            URL url = new URL(str);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
