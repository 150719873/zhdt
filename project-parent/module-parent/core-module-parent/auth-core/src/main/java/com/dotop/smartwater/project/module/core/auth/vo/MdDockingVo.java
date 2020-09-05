package com.dotop.smartwater.project.module.core.auth.vo;


import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MdDockingVo extends BaseVo {
    private String id;
    private String category;
    private String type;
    private String code;
    private String username;
    private String password;
    private String host;
    private String url;
    private String time;
    private Integer rang;
    private String waterUsername;
    private String waterPassword;
    private String waterHost;
    private String system;
    private String mode;
    private String factory;
    private String productName;

    private String enterpriseName;
    private String mdeId;
}