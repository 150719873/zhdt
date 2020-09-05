package com.dotop.smartwater.project.module.core.third.form.iot;

import lombok.Data;

/**
 * @program: project-parent
 * @description: IOT发送过来的NB信息

 * @create: 2019-03-30 10:31
 **/
@Data
public class MeterNb {
    /**
     * 版本
     * */
    private String ver;
    /**
     * 基站id
     * */
    private String cellId;
    /**
     * 小区局域id
     * */
    private String pci;

    private String signalPower;
    private String totalPower;
    private String txPower;
    private String txTime;
    private String rxTime;
    private String ecl;
    private String snr;
    private String earfcn;
    private String rsrq;
    private String operatorMode;
    private String imsi;

}
