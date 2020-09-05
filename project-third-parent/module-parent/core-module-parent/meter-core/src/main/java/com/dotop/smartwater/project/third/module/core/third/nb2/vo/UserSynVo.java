package com.dotop.smartwater.project.third.module.core.third.nb2.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSynVo extends BaseVo {

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
    private String meterNo;

    /**
     * 用户地址
     */
    private String userAddr;

    /**
     * 用户地址
     */
    private String type;
}
