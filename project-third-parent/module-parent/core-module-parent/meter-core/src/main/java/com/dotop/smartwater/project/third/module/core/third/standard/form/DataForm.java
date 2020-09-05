package com.dotop.smartwater.project.third.module.core.third.standard.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataForm extends BaseForm {

    /**
     * 月份
     */
    private String month;

    /**
     * 用户表号
     */
    private String devno;

    /**
     * 开始年月
     */
    private String startMonth;

    /**
     * 结束年月
     */
    private String endMonth;

}
