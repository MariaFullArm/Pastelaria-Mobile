package br.com.fulltime.projeto.foodtruck.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DataUtil {

    public static final SimpleDateFormat FORMATO_UNIVERSAL = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat FORMATO_UNIVERSAL2 = new SimpleDateFormat("yyyy-MM-dd");

    public static String formataCalendar(Calendar calendar) {
        return FORMATO_UNIVERSAL2.format(calendar.getTime());
    }

    public static Date converteParaDataApi(java.util.Date data){
        return new java.sql.Date(Long.parseLong(FORMATO_UNIVERSAL.format(data)));
    }
}
