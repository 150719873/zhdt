package com.dotop.pipe.core.vo.third;

import lombok.Data;

/**
 *
 */
@Data
public class KentDataVo {

    /// 站点名称

    public String StationName;

    /// 出厂编号

    public String MeterNumber;

    /// GPRSID（通讯模块号）

    public String GPRSNo;

    /// 数据读取时间

    public String DataTime;

    /// 瞬时流量

    public String InstantFlow;

    /// 压力

    public String Pressure;

    /// 流速

    public String Speed;

    /// 正向累计

    public String TotalFlowF;

    /// 反向累计

    public String TotalFlowR;

    /// 水表电量

    public String MeterBat;

    /// GPRS模块电量

    public String GprsBat;

    /// 信号强度

    public String GprsDbm;


    public String JLL;


    public String InsertTime;
}
