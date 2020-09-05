package com.dotop.smartwater.project.module.core.auth.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlatformRoleDto extends BaseDto {
	private String proleid;

	private String name;

	private String description;

	private String createuser;

	private Date createtime;
}
