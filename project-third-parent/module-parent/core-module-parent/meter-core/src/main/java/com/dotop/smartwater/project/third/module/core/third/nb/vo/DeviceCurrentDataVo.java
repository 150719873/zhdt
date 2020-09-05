package com.dotop.smartwater.project.third.module.core.third.nb.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceCurrentDataVo extends BaseVo {

    /**
     * id
     */
    private String id;

    /**
     * IMEI号
     */
    private String imei;

    /**
     * 设备编号，设备上传数据后可能被更改
     */
    private String deviceId;

    /**
     * Json字符串
     */
    private String returnCommand;

    /**
     * 数据接收时间，格式：YYYYMMDDhhmmss
     */
    private String reciveTime;
}
