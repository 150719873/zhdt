package com.dotop.smartwater.project.third.module.core.third.lora.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class MeterChangeVo extends BaseVo {

    /**
     * 记录id
     */
    private Integer recordId;
    /**
     * 水表id
     */
    private Integer meterId;
    /**
     * 旧表最终读数
     */
    private String oldMeterData;
    /**
     * 新表初始读数
     */
    private String newMeterData;
    /**
     * 换表时间
     */
    private Date operateTime;
    /**
     * 操作员
     */
    private String operateMan;
    /**
     * 安装地址
     */
    private String meterAddr;
    /**
     * 记录状态：1-正常 2-新增
     */
    private Integer recordFlag;
    /**
     * 备注
     */
    private String remark;
}
