package com.dotop.smartwater.project.third.module.core.third.video.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MeterVo  extends BaseVo {

    /**
     * 智能水表记录id
     */
    private Integer id;

    /**
     * 智能水表编号
     */
    private String meterid;

    /**
     * 智能水表单位系数（立方米）
     */
    private String unit;

    /**
     * 智能水表口径
     */
    private String caliber;

    /**
     * 智能水表型号
     */
    private String product;

    /**
     * 智能水表表底
     */
    private Double basecount;

    private IDeviceVo device;

}
