package com.dotop.smartwater.project.third.module.core.third.video.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class IMeterVo extends BaseVo {

    /**
     * 主键id
     */
    private String id;

    /**
     * 客户使用的水表编号
     */
    private String meterid;

    /**
     *客户开始使用该水表时间
     */
    private Date starttime;

    /**
     *客户停止使用该水表时间
     */
    private Date endtime;
}
