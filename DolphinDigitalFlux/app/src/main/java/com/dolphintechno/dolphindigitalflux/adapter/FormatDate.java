package com.dolphintechno.dolphindigitalflux.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatDate {

    public static Date longFormat(String value) throws ParseException{

        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy 'at' HH:mm'am'", Locale.ENGLISH);

        return dateFormat.parse(value);
    }

}
