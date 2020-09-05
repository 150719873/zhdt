package com.dotop.smartwater.project.third.module.core.third.standard.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthForm extends BaseForm {

    /**
     * 单位代码
     */
    private String code;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
