package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReportRoleBindDto extends BaseDto {

	private String id;

	private String roleid;

	private String bindid;
}
