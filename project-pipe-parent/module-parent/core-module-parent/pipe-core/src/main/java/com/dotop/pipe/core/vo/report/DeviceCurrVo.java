package com.dotop.pipe.core.vo.report;


import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceCurrVo extends BasePipeVo {

    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备编号
     */
    private String code;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 区域
     */
    private String area;
    /**
     * 设备上报数据
     */
    private DevicePropertyVo deviceProperty;
    /**
     * 报警次数
     */
    private Integer alarmNum;
    /**
     * 设备类别
     */
    private String productCategory;
    /**
     * 设备类型
     */
    private String productType;
}
