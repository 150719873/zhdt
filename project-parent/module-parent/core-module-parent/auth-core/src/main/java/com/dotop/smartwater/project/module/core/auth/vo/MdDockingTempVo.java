package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MdDockingTempVo extends BaseVo {
    private String id;

    private String category;

    private String type;

    private String typeName;

    private String host;

    private String url;

    private String time;

    private Integer rang;

    private String factory;

    private Boolean status;

    private Boolean checked;
}