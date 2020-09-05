package com.dotop.smartwater.project.third.module.core.third.nb2.vo;


import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceUplinkVo extends BaseVo {

    /**
     * 用户编号
     */
    private String Userid;

    /**
     * 用户姓名
     */
    private String Username;

    /**
     * 用户地址
     */
    private String UserAddr;

    /**
     * 阀门状态0 关闭 1开启 -1  未知
     */
    private Integer ValveStatus;

    /**
     * 气表编号
     */
    private String Meterno;

    /**
     * 止度
     */
    private String MeterDegrees;

    /**
     * 抄表时间
     */
    private String ReadDate;
}
