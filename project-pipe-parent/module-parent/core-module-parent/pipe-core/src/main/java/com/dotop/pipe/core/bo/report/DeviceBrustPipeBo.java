package com.dotop.pipe.core.bo.report;


import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBrustPipeBo extends BasePipeBo {

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
    /**
     * 查询开始时间
     */
    private Date startDate;
    /**
     * 查询结束时间
     */
    private Date endDate;
}
