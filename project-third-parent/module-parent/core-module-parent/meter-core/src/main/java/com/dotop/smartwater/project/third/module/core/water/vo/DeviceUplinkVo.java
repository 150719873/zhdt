package com.dotop.smartwater.project.third.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceUplinkVo extends BaseVo {

    /**
     * 主键id
     */
    private String id;

    /**
     * 业主编号
     */
    private String userno;

    /**
     * 设备id
     */
    private String devid;

    /**
     * 第三方id
     */
    private String thirdid;

    /**
     * 水表
     */
    private String devno;

    /**
     * 设备号
     */
    private String deveui;

    /**
     * 读数
     */
    private Double water;

    /**
     * 最后更新时间
     */
    private Date uplinkDate;

    /**
     * 返回数据的json窜
     */
    private String json;

    /**
     * 安装地址
     */
    private String devaddr;

    /**
     * 阀门类型
     */
    private Integer tapstatus;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 图片(视频直读)
     */
    private String url;

    /**
     * iccid
     */
    private String iccid;


    /**
     * 针对流量计补充字段
     */
    /**
     * 无水异常：0,正常、1,异常
     */
    private String anhydrousAbnormal;
    /**
     * 断线异常：0,正常、1,异常
     */
    private String disconnectionAbnormal;
    /**
     * 压力异常：0,正常、1,异常
     */
    private String pressureException;
    /**
     * 厂家代码
     */
    private String factory;
    /**
     * 流速，单位升/时
     */
    private String flowRate;
    /**
     * 管道压力，单位：Kpa
     */
    private String pressure;
    /**
     * 累计工作时间，单位：小时
     */
    private String totalWorkTime;
    /**
     * 管道水温，单位：度
     */
    private String temperature;
    /**
     * 水表内部时钟
     */
    private String internalClock;

    /**
     * 请求参数全部转换为json
     */
    private String uplinkData;

    /**
     * 信号强度
     */
    private String rssi;
    /**
     * 信号强度
     */
    private String lsnr;
}
