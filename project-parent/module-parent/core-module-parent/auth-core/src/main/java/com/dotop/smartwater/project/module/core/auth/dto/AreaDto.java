package com.dotop.smartwater.project.module.core.auth.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AreaDto extends BaseDto {

	private String id;

	private String pId;

	private String name;
	//区域编号
	private String code;
}
