package com.dotop.pipe.core.vo.third;

import lombok.Data;

/**
 *
 */
@Data
public class TancyDataVo {

    /// 设备编号

    public String ID;

    /// 通讯编号

    public String Entry;

    /// 用户号

    public String UserID;

    /// 采集时间

    public String Date;

    /// 瞬时流量

    public String Instantaneous;

    /// 正向累计流量

    public String Positive;

    /// 反向累计流量

    public String Reverse;

    /// 压力通道1

    public String Pressure1;

    /// 压力通道2

    public String Pressure2;

    /// 电池电压

    public String Voltage;

    /// 信号强度

    public String DeviceSignal;

    /// 设备地址

    public String Address;

    /// 经度

    public String Longitude;

    /// 纬度

    public String Latitude;
}
