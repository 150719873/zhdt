package com.dotop.smartwater.project.third.module.core.third.nb2.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSynForm extends BaseForm {

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
     * 用户编号
     */
    private String userid;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 气表编号
     */
    private String meterno;

    /**
     * 用户地址
     */
    private String userAddr;

    /**
     * 用户地址
     */
    private String type;
}
