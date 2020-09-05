package com.dotop.smartwater.project.module.core.auth.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class MdDockingTempBo extends BaseBo {
    private String id;

    private String category;

    private String type;

    private String typeName;

    private String host;

    private String url;

    private Date time;

    private Integer rang;

    private String createBy;

    private Date createDate;

    private String lastBy;

    private Date lastDate;

    private String factory;

    private Boolean status;
}