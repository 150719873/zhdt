package com.dotop.smartwater.view.server.core.device.vo;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceVo extends BasePipeVo {

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
     * 产品类别
     */
    private String productCategory;
    /**
     * 产品类型
     */
    private String productType;


}