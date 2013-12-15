package com.petrpopov.opennlprus.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by petrpopov on 15.12.13.
 */
public class ThreadSafeSimpleDateFormat {

    private static final Locale RUS_LOCALE = new Locale("ru");

    private DateFormat enFormat;
    private DateFormat rusFormat;

    public ThreadSafeSimpleDateFormat(String format) {
        this.enFormat = new SimpleDateFormat(format, Locale.US);
        this.rusFormat = new SimpleDateFormat(format, RUS_LOCALE);
    }

    public synchronized void setLenient(boolean flag) {
        enFormat.setLenient(flag);
        rusFormat.setLenient(flag);
    }

    public synchronized String format(Date date) {
        return enFormat.format(date);
    }

    public synchronized Date parse(String string, Locale locale) throws ParseException {

        if( locale.equals(RUS_LOCALE) ) {
            return rusFormat.parse(string);
        }
        else {
            return enFormat.parse(string);
        }
    }
}
