package com.dotop.smartwater.project.third.module.core.third.nb.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceInfoVo extends BaseVo {
    /**
     *id
     */
    private String id;

    /**
     *IMEI号
     */
    private String imei;

    /**
     *电信IoT产生的平台编码，为空则表示在电信IoT平台注册失败
     */
    private String platformId;

    /**
     *设备编号，由操作者在NB抄表平台自定义输入
     */
    private String deviceId;

    /**
     *安装地址
     */
    private String location;

    /**
     *设备名字，由操作者在NB抄表平台自定义输入
     */
    private String name;

    /**
     *安装日期，格式：YYYYMMDD
     */
    private Date createTime;

}
