package com.dotop.smartwater.project.module.core.auth.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class MdDockingDto extends BaseDto {
    private String id;
    private String category;
    private String type;
    private String code;
    private String username;
    private String password;
    private String host;
    private String url;
    private Date time;
    private Integer rang;
    private String createBy;
    private Date createDate;
    private String lastBy;
    private Date lastDate;
    private String waterUsername;
    private String waterPassword;
    private String waterHost;
    private String system;
    private String mode;
    private String factory;
    private String productName;

    private String mdeId;
}