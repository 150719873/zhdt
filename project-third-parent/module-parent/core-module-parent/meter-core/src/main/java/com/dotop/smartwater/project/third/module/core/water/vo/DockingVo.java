package com.dotop.smartwater.project.third.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DockingVo extends BaseVo {

    /**
     * 主键id
     */
    private String id;

    /**
     * 类别
     */
    private String category;

    /**
     * 类型
     */
    private String type;

    /**
     * 水司机构代码
     */
    private String code;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * host
     */
    private String host;

    /**
     * 水务平台host
     */
    private String waterHost;

    /**
     * 接口名
     */
    private String url;

    /**
     * 定时到某一刻执行
     */
    private Date time;

    /**
     * 每隔多少分钟执行
     */
    private Integer rang;

    /**
     * 水务平台用户名
     */
    private String waterUsername;

    /**
     * 水务平台密码
     */
    private String waterPassword;


    /**
     * 系统类型
     */
    private String system;

    /**
     * 通信方式
     */
    private String mode;
    /**
     * 水表厂家
     */
    private String factory;
    /**
     * 产品名称
     */
    private String productName;
}
