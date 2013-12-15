package com.petrpopov.opennlprus.support;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by petrpopov on 15.12.13.
 */

@Component
public class DateUtil {

    private static final Locale DEFAULT_LOCALE = Locale.getDefault(Locale.Category.FORMAT);
    private static final Locale RUS_LOCALE = new Locale("ru");

    private static List<ThreadSafeSimpleDateFormat> dateFormats = new ArrayList<ThreadSafeSimpleDateFormat>() {
        {
            add(new ThreadSafeSimpleDateFormat("M/dd/yyyy"));
            add(new ThreadSafeSimpleDateFormat("dd.M.yyyy"));
            add(new ThreadSafeSimpleDateFormat("dd.MM.yyyy"));

            add(new ThreadSafeSimpleDateFormat("dd-MM-yyyy"));
            add(new ThreadSafeSimpleDateFormat("MM-dd-yyyy"));
            add(new ThreadSafeSimpleDateFormat("yyyy-MM-dd"));
            add(new ThreadSafeSimpleDateFormat("yyyy-dd-MM"));

            add(new ThreadSafeSimpleDateFormat("hh:mm dd.MM.yyyy"));

            add(new ThreadSafeSimpleDateFormat("dd/MM/yyyy"));
            add(new ThreadSafeSimpleDateFormat("MM/dd/yyyy"));
            add(new ThreadSafeSimpleDateFormat("yyyy/MM/dd"));
            add(new ThreadSafeSimpleDateFormat("yyyy/dd/MM"));

            add(new ThreadSafeSimpleDateFormat("EEE, d MMM yyyy hh:mm:ss Z"));
            add(new ThreadSafeSimpleDateFormat("EEE, d MMM yyyy"));

            add(new ThreadSafeSimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z"));
            add(new ThreadSafeSimpleDateFormat("EEE, MMM d, ''yy"));
            add(new ThreadSafeSimpleDateFormat("h:mm a"));
            add(new ThreadSafeSimpleDateFormat("hh 'o''clock' a, zzzz"));
            add(new ThreadSafeSimpleDateFormat("K:mm a, z"));
            add(new ThreadSafeSimpleDateFormat("yyyyy.MMMMM.dd GGG hh:mm aaa"));
            add(new ThreadSafeSimpleDateFormat("yyMMddHHmmssZ"));
            add(new ThreadSafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            add(new ThreadSafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
            add(new ThreadSafeSimpleDateFormat("YYYY-'W'ww-u"));

            add(new ThreadSafeSimpleDateFormat("EEE, d MMM yyyy hh:mm:ss aaa"));
            add(new ThreadSafeSimpleDateFormat("EEE, d MMM yyyy hh:mm aaa"));
            add(new ThreadSafeSimpleDateFormat("EEE, d MMM yyyy hh:mm:ss aaa Z"));
            add(new ThreadSafeSimpleDateFormat("EEE, d MMM yyyy hh:mm aaa Z"));

            add(new ThreadSafeSimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
            add(new ThreadSafeSimpleDateFormat("dd.M.yyyy hh:mm:ss a"));

            add(new ThreadSafeSimpleDateFormat("dd.MMM.yyyy"));
            add(new ThreadSafeSimpleDateFormat("dd-MMM-yyyy"));
        }
    };

    public boolean isDateString(String text) {

        if( text == null ) {
            return false;
        }

        String str = text.trim();

        if(Strings.isNullOrEmpty(str) ) {
            return false;
        }

        Date date = convertToDate(str);
        if( date == null ) {
            return false;
        }

        return true;
    }

    public Date convertToDate(String input) {

        Date enDate = null;
        Date rusDate = null;

        if(null == input) {
            return null;
        }

        for (ThreadSafeSimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                enDate = format.parse(input, Locale.US);
            } catch (ParseException e) {
                //try other
            }

            try {
                format.setLenient(false);
                rusDate = format.parse(input, RUS_LOCALE);
            } catch (ParseException e) {
                //try other
            }

            if (enDate != null || rusDate != null) {
                break;
            }
        }

        if( rusDate != null && enDate != null ) {
            if( DEFAULT_LOCALE.equals(RUS_LOCALE) ) {
                return rusDate;
            }
            else {
                return enDate;
            }
        }
        else if( enDate == null )
            return rusDate;
        return enDate;
    }
}
