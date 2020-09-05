package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBookManagementBo extends BaseBo {
    private String bookId;

    private String bookNum;

    private String bookUserId;

    private String bookType;

    private String createBy;

    private Date createDate;

    private String lastBy;

    private Date lastDate;

    private Integer isDel;

    private String userName;
}