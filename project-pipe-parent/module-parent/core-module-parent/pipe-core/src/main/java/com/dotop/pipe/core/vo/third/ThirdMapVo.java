package com.dotop.pipe.core.vo.third;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

//第三方与传感器关系
@Data
@EqualsAndHashCode(callSuper = false)
public class ThirdMapVo extends BasePipeVo {

    // 主键
    private String mapId;

    // 设备主键
    private String deviceId;

    // 设备编号
    private String deviceCode;

    // 第三方主键
    private String thirdId;

    // 第三方编码
    private String thirdCode;

    // 设备类型
    private String deviceType;

    /**
     * 厂商名称
     */
    private String factoryName;

    /**
     * 通信协议
     */
    private String protocol;
}
