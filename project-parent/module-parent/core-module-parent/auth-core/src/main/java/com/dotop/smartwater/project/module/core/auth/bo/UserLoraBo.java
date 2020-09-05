package com.dotop.smartwater.project.module.core.auth.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserLoraBo extends BaseBo {

    private String id;
    private String enterpriseid;
    private String account;
    private String password;
    private String appeui;

}
