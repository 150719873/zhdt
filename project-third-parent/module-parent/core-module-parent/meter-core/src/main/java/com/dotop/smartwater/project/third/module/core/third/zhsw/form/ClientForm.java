package com.dotop.smartwater.project.third.module.core.third.zhsw.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientForm extends BaseForm {

    /**
     * 主键id
     */
    private String id;

    /**
     * 水表编号
     */
    private String wcode;

    /**
     * 水表类型
     */
    private String wtype;

    /**
     * 水表型号
     */
    private String wmodel;

    /**
     * 用水类型
     */
    private String water_type;

    /**
     * 安装地址
     */
    private String install_address;

    /**
     * 安装时间
     */
    private int install_time;

    /**
     * 客户姓名
     */
    private String client_name;

    /**
     * 客户编码
     */
    private String client_code;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
     */
    private String certificates_code;

    /**
     * 口径
     */
    private BigDecimal caliber;

    /**
     * 最新抄表时间
     */
    private int current_time;

    /**
     * 最新抄表读数
     */
    private BigDecimal current_num;

}
