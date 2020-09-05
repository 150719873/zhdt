package com.dotop.smartwater.project.third.module.core.third.standard.vo;


import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataVo extends BaseVo {

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


    /**
     * 用户表号
     */
    private String devno;

    /**
     * 水表信息列表
     */
    private List<UplinkVo> uplinks;
}
