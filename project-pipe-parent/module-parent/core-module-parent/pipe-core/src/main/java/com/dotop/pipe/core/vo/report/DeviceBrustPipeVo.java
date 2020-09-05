package com.dotop.pipe.core.vo.report;


import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBrustPipeVo extends BasePipeVo {

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
     * 区域名称
     */
    private String area;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 统计天数
     */
    private Integer countDays;
    /**
     * 爆管次数
     */
    private Integer brustNum;
    /**
     * 设备类别
     */
    private String productCategory;
    /**
     * 设备类型
     */
    private String productType;
}
