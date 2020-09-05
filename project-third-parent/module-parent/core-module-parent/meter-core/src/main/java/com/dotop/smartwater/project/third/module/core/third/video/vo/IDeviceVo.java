package com.dotop.smartwater.project.third.module.core.third.video.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IDeviceVo extends BaseVo {

    /**
     * 主键id
     */
    private String id;

    /**
     * 设备编号
     */
    private String deviceid;

    /**
     * 设备抄表模式
     */
    private String sendmode;

    /**
     * 设备电量
     */
    private Integer battery;

    /**
     * 设备是否启动状态
     */
    private String openstatus;

    /**
     * 设备运行状态（设备反馈信息）
     */
    private Integer status;

    /**
     * 设备对应的SIM号
     */
    private String SIM;

}
