package com.dotop.pipe.core.vo.device;

import java.util.Date;

import lombok.Data;

@Data
public class DevicePropertyVo {

    // 主键
    private String devProId;

    // 设备主键
    private String deviceId;

    // 设备编号
    private String deviceCode;

    // 表字段名
    private String field;

    // 传输协议TAG
    private String tag;

    // 名称
    private String name;

    /**
     * 单位
     */
    private String unit;
    // 类型
    private String type;

    // 值
    private String val;

    // 设备发送时间
    private Date devSendDate;

    // 服务器接收时间
    private Date serReceDate;


    private String flwRate;
    private String flwTotalValue;
    private String flwMeasure;
    private String pressureValue;
    private String qualityTemOne;
    private String qualityTemTwo;
    private String qualityTemThree;
    private String qualityTemFour;
    private String qualityChlorine;
    private String qualityOxygen;
    private String qualityPh;
    private String qualityTurbid;
    private String highLowAlarm;
    private String slope;
    private String bump;

}
