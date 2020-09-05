package com.dotop.smartwater.dependence.core.utils;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**

 * @date 2019年5月8日
 * @description 日期工具类
 */
public class DateUtils {

    public static final String YYYY_MM = "yyyy-MM";

    public static final String YYYYMM = "yyyyMM";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String DATE = "yyyy-MM-dd";

    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter FORMAT_DATE = DateTimeFormat.forPattern(DATE);

    public static final DateTimeFormatter FORMAT_DATETIME = DateTimeFormat.forPattern(DATETIME);

    private DateUtils() {
        super();
    }


    /**
     * 计算时间内的分表日期
     */
    public static Set<String> listDates(Date startDate, int diff) {
        Date endDate = month(startDate, diff);
        return listDates(startDate, endDate);
    }

//    public static void main(String[] args) {
//        Date startDate = DateUtils.parseDatetime("2019-09-25 22:22:22");
//        Date endDate = DateUtils.parseDatetime("2019-11-24 22:23:22");
//        System.out.println(listDates(startDate, endDate));
//    }

    /**
     * 计算时间内的分表日期
     */
    public static Set<String> listDates(Date startDate, Date endDate) {
        LocalDate localDateStart = LocalDate.fromDateFields(startDate);
        LocalDate localDateEnd = LocalDate.fromDateFields(endDate);
        int months = Months.monthsBetween(localDateStart, localDateEnd).getMonths();
        Period periodJoda = Period.fieldDifference(localDateStart, localDateEnd);
//        System.out.println(months + ":" + periodJoda.getDays());
        if (periodJoda.getDays() < 0) {
            months += 1;
        }
        Set<String> listDates = new HashSet<>();
        for (int i = 0; i <= months; i++) {
            LocalDate localDate = localDateStart.plusMonths(i);
            listDates.add(localDate.toString("yyyyMM"));
        }
        return listDates;
    }

    /**
     * 时间秒数差
     */
    public static final int secondsBetween(Date date0, Date date1) {
        DateTime dateTime0 = new DateTime(date0);
        DateTime dateTime1 = new DateTime(date1);
        return Seconds.secondsBetween(dateTime0, dateTime1).getSeconds();
    }

    /**
     * 时间小时数差
     */
    public static final int hoursBetween(Date date0, Date date1) {
        DateTime dateTime0 = new DateTime(date0);
        DateTime dateTime1 = new DateTime(date1);
        return Hours.hoursBetween(dateTime0, dateTime1).getHours();
    }

    /**
     * 时间天数差
     */
    public static final int daysBetween(Date date0, Date date1) {
        DateTime dateTime0 = new DateTime(date0);
        DateTime dateTime1 = new DateTime(date1);
        return Days.daysBetween(dateTime0, dateTime1).getDays();
    }

    /**
     * 时间月数差
     */
    public static final int monthsBetween(Date date0, Date date1) {
        DateTime dateTime0 = new DateTime(date0);
        DateTime dateTime1 = new DateTime(date1);
        return Months.monthsBetween(dateTime0, dateTime1).getMonths();
    }

    /**
     * 时间的年
     */
    public static final Date year(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
                .toDate();
    }

    /**
     * 时间的年操作
     */
    public static final Date year(Date date, int year) {
        DateTime dateTime = new DateTime(date);
        return dateTime.withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
                .plusYears(year).toDate();
    }

    /**
     * 时间的年操作
     */
    public static final String year(Date date, int year, String format) {
        return format(year(date, year), format);
    }

    /**
     * 时间的月
     */
    public static final Date month(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
    }

    /**
     * 时间的月操作
     */
    public static final Date month(Date date, int month) {
        DateTime dateTime = new DateTime(date);
        return dateTime.withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).plusMonths(month)
                .toDate();
    }

    /**
     * 时间的月操作
     */
    public static final String month(Date date, int month, String format) {
        return format(month(date, month), format);
    }

    /**
     * 时间的天
     */
    public static final Date day(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
    }

    /**
     * 时间的天操作
     */
    public static final Date day(Date date, int day) {
        DateTime dateTime = new DateTime(date);
        return dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).plusDays(day).toDate();
    }

    /**
     * 时间的天操作
     */
    public static final String day(Date date, int day, String format) {
        return format(day(date, day), format);
    }

    /**
     * 时间的时
     */
    public static final Date hour(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.withMinuteOfHour(0).withSecondOfMinute(0).toDate();
    }

    /**
     * 时间的时操作
     */
    public static final Date hour(Date date, int hour) {
        DateTime dateTime = new DateTime(date);
        return dateTime.withMinuteOfHour(0).withSecondOfMinute(0).plusHours(hour).toDate();
    }

    /**
     * 时间的时操作
     */
    public static final String hour(Date date, int hour, String format) {
        return format(hour(date, hour), format);
    }

    /**
     * 时间的秒
     */
    public static final Date second(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toDate();
    }

    /**
     * 时间的秒操作
     */
    public static final Date second(Date date, int second) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(second).toDate();
    }

    /**
     * 时间的秒操作
     */
    public static final String second(Date date, int second, String format) {
        return format(second(date, second), format);
    }

    /**
     * 时间比较
     * <p>
     * 如果true，date0>date1
     * <p>
     * 如果false，date0<date1
     */
    public static final Boolean compare(Date date0, Date date1) {
        DateTime dateTime0 = new DateTime(date0);
        DateTime dateTime1 = new DateTime(date1);
        return dateTime0.isAfter(dateTime1);
    }

    /**
     * 格式化 yyyy-MM-dd HH:mm:ss
     */
    public static final Date parseDatetime(String str) {
        DateTime dateTime = DateTime.parse(str, FORMAT_DATETIME);
        return dateTime.toDate();
    }

    /**
     * 格式化 yyyy-MM-dd
     */
    public static final Date parseDate(String str) {
        DateTime dateTime = DateTime.parse(str, FORMAT_DATE);
        return dateTime.toDate();
    }

    /**
     * 自定义格式化
     */
    public static final Date parse(String str, String format) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = DateTime.parse(str, formatter);
        return dateTime.toDate();
    }

    /**
     * 格式化 yyyy-MM-dd HH:mm:ss
     */
    public static final String formatDatetime(Date date) {
        return new DateTime(date).toString(FORMAT_DATETIME);
    }

    /**
     * 格式化 yyyy-MM-dd
     */
    public static final String formatDate(Date date) {
        return new DateTime(date).toString(FORMAT_DATE);
    }

    /**
     * 格式化 yyyy-MM
     */
    public static final String formatDateMonth(Date date) { return new DateTime(date).toString(YYYY_MM);}
    
    /**
     * 自定义格式化
     */
    public static final String format(Date date, String format) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        return new DateTime(date).toString(formatter);
    }

    public static final Date date(String type, Date date) {
        switch (type) {
            case "YEAR":
                return DateUtils.year(date);
            case "MONTH":
                return DateUtils.month(date);
            case "DAY":
                return DateUtils.day(date);
            case "HOUR":
                return DateUtils.hour(date);
            default:
                return date;
        }
    }

    public static final Date date(String type, Date date, int i) {
        switch (type) {
            case "YEAR":
                return DateUtils.year(date, i);
            case "MONTH":
                return DateUtils.month(date, i);
            case "DAY":
                return DateUtils.day(date, i);
            case "HOUR":
                return DateUtils.hour(date, i);
            default:
                return date;
        }
    }
}
