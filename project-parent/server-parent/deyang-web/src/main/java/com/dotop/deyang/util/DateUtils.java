package com.dotop.deyang.util;



import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class DateUtils {
    /**
     * 自定义格式化
     */
    public final static String YYYYMM = "yyyyMM";
    public final static String DATE = "yyyy-MM-dd";
    public final static String format(String str, String format, String to) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = DateTime.parse(str, formatter);
        formatter = DateTimeFormat.forPattern(to);
        return dateTime.toString(formatter);
    }
}
