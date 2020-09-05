package com.dotop.smartwater.project.third.module.core.third.nb2.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataBackForm extends BaseForm {

    /**
     * 单位代码
     */
    private String unitcode;

    /**
     * 账号
     */
    private String uname;

    /**
     * 密码
     */
    private String password;

    /**
     * 月份
     */
    private String yf;

    /**
     * 用户表号
     */
    private String meterNo;

    /**
     * 用户编号
     */
    private String userid;

    /**
     * 开始时间
     */
    private String meterStime;

    /**
     * 结束时间
     */
    private String meterEtime;
}
