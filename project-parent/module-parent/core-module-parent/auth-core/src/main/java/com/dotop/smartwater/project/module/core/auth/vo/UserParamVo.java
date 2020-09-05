package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserParamVo extends BaseVo {
	private String userid;
	private String ticket;
	private String enterpriseid;
	private String account;
	private String name; 
	private Integer type;
	private String roleid;
	private Boolean calibration;
	//用户绑定权限
	private String bindpermission;
}
