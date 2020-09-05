package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionVo extends BaseVo {
	private String id;
    private String pId;
    private String name;
    private boolean open;
    private boolean checked;
    private Integer type;
}
