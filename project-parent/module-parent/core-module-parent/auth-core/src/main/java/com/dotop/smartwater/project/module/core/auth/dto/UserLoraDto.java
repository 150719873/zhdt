package com.dotop.smartwater.project.module.core.auth.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserLoraDto extends BaseDto {

	private String id;
	private String enterpriseid;
	private String account;
	private String password;
	private String appeui;
}
