package com.dotop.pipe.core.bo.third;


import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ThirdMapBo extends BasePipeForm {

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

    // 产品类别
    private String productCategory;

    // 产品类型
    private String productType;

    // 厂商编码
    private String factoryCode;

    private String startDt;

    private String endDt;

    /**
     * 厂商名称
     */
    private String factoryName;
}
