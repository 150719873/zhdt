package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBookManagementVo extends BaseVo {
    private String bookId;

    private String bookNum;

    private String bookUserId;

    private String bookType;

    private String createBy;

    private Date createDate;

    private String lastBy;

    private Date lastDate;

    private Integer isDel;

    //格外字段
    private String userName;
    private String childName;
    private String workNum;
}