package com.petrpopov.opennlprus.service;

import com.google.common.base.Strings;
import com.petrpopov.opennlprus.util.DateUtil;
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

    private static final Integer TIME_LENGTH = 6;

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
            if( Strings.isNullOrEmpty(clean) )
                continue;

            boolean isDate = isDateString(clean);
            if( isDate )
                continue;

            boolean isUrl = isURLString(clean);
            if( isUrl )
                continue;

            String cleanTime = cleanTime(clean);
            String cleanQuotes = cleanQuotes(cleanTime);
            String cleanCopyright = cleanCopyright(cleanQuotes);

            if( cleanCopyright.length() <= LENGTH )
                continue;

            res.add(cleanCopyright);
        }

        return res;
    }

    private String clean(String str) {

        String replace = str.replaceAll(LEFT_GARBAGE, "");
        replace.replaceAll(RIGHT_GARBAGE,"");
        replace = replace.trim();

        return replace;
    }

    private String cleanTime(String str) {
        if( str.length() < TIME_LENGTH )
            return str;

        String substring = str.substring(0, TIME_LENGTH);

        boolean date = isDateString(substring.trim());
        if( date ) {
            return str.substring(TIME_LENGTH);
        }

        return str;
    }

    private String cleanQuotes(String str) {

        String quotes = str.replaceAll("\"", "");

        int diff = quotes.length() - str.length();
        if( diff > 0 ) {
            if( diff % 2 != 0 ) {
                if( str.charAt(0) == '"' ) {
                    return str.substring(1);
                }
                else if( str.charAt(str.length()-1) == '"') {
                    return str.substring(0, str.length()-1);
                }
                else return str;
            }
        }

        return str;
    }

    private String cleanCopyright(String str) {
        if( str.contains("©"))
            return "";
        return str;
    }

    private boolean isDateString(String str) {
        return dateUtil.isDateString(str);
    }

    private boolean isURLString(String str) {

        try {
            new URL(str);
        } catch (MalformedURLException e) {
            return false;
        }


        return true;
    }
}
