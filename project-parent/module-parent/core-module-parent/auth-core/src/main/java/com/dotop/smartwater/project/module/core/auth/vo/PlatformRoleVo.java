package com.dotop.smartwater.project.module.core.auth.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class PlatformRoleVo extends BaseVo {
	private String proleid;

	private String name;

	private String description;

	private String createuser;

	private Date createtime;
}
