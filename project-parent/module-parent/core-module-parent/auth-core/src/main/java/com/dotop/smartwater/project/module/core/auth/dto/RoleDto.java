package com.dotop.smartwater.project.module.core.auth.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同role
 * 
 * 合并 旧原名 RoleParam
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleDto extends BaseDto {
	private String roleid;

	private String name;

	private String description;

	private Date createtime;

	private String createuser;

	private String permissionids;

}