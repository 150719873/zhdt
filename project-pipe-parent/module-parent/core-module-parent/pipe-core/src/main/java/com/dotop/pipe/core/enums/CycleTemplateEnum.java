package com.dotop.pipe.core.enums;

import com.dotop.smartwater.dependence.core.utils.DateUtils;
import lombok.Getter;

import java.util.Date;

/**
 *
 * @date 2020/06/4.
 */
public enum CycleTemplateEnum {

    /**
     * 每日
     */
    EVERY_DAY("EVERY_DAY", "每日", "EVERY_DAY"),

    /**
     * 每周
     */
    EVERY_WEEK("EVERY_WEEK", "每周", "EVERY_WEEK"),

    /**
     * 每月
     */
    EVERY_MONTH("EVERY_MONTH", "每月", "EVERY_MONTH"),

    /**
     * 每年
     */
    EVERY_YEAR("EVERY_YEAR", "每年", "EVERY_YEAR"),

    /**
     * 自定义
     */
    CUSTOM("CUSTOM", "自定义", "CUSTOM");


    @Getter
    private String id;
    @Getter
    private String name;
    @Getter
    private String code;

    CycleTemplateEnum(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    /**
     * 计算下一次执行日
     *
     * @param cycleTemplate
     * @param basicTime
     * @param cycle
     * @param now
     * @return
     */
    public static Date getNextDate(CycleTemplateEnum cycleTemplate, Date basicTime, Integer cycle, Date now) {
        int i = DateUtils.daysBetween(basicTime, now);
        if (i >= 0 && basicTime.getTime() >= now.getTime()) {
            return basicTime;
        }
        String time = DateUtils.format(basicTime, "HH:mm:ss");
        switch (cycleTemplate) {
            case EVERY_DAY:
                return DateUtils.parseDatetime(DateUtils.formatDate(now) + " " + time);
            case EVERY_WEEK:
                String date;
                if (i % 7 == 0) {
                    date = DateUtils.formatDate(DateUtils.day(basicTime, i));
                } else {
                    date = DateUtils.formatDate(DateUtils.day(basicTime, i + 7 - i % 7));
                }
                return DateUtils.parseDatetime(date + " " + time);
            case EVERY_MONTH:
                return DateUtils.parseDatetime(DateUtils.format(now, "yyyy-MM")
                        + "-" + DateUtils.format(basicTime, "dd") + " "
                        + time);
            case EVERY_YEAR:
                return DateUtils.parseDatetime(DateUtils.format(now, "yyyy") + "-"
                        + DateUtils.format(basicTime, "MM-dd HH:mm:ss"));
            case CUSTOM:
                String date2;
                if (i % 7 == 0) {
                    date2 = DateUtils.formatDate(now);
                } else {
                    date2 = DateUtils.formatDate(DateUtils.day(basicTime, i + cycle - i % cycle));
                }
                return DateUtils.parseDatetime(date2 + " " + time);
            default:
                return basicTime;
        }
    }
}
