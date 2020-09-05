package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同role
 * 
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleVo extends BaseVo {
	private String roleid;

	private String name;

	private String description;

	private String createtime;

	private String createuser;

}