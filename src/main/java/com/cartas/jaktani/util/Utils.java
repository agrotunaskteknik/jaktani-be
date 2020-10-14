package com.cartas.jaktani.util;

import java.sql.Timestamp;
import java.util.Calendar;

public class Utils {
    public static Calendar getCalendar(){
        return Calendar.getInstance();
    }

    public static Timestamp getTimeStamp(Long time){
        return new Timestamp(time);
    }

    // test update git
}
