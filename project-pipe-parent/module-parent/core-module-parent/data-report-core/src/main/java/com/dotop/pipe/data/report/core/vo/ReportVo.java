package com.dotop.pipe.data.report.core.vo;

import lombok.Data;

/**
 *
 * @date 2018/11/2.
 */
@Data
public class ReportVo {
    public String deviceId;
    public String field;
    public String val;
    public String sumVal;
    public String maxVal;
    public String minVal;
    public String avgVal;
    public String sendYear;
    public String sendMonth;
    public String sendDay;
    public String sendHour;
    public String sendDate;
    public String flwMeasure;
    public String flwRate;
    public String flwTotalValue;
    public String pressureValue;
    public String qualityChlorine;
    public String qualityOxygen;
    public String qualityPh;
    public String qualityTurbid;
    public String qualityTemOne;
    public String qualityTemTwo;
    public String qualityTemThree;
    public String qualityTemFour;
}
