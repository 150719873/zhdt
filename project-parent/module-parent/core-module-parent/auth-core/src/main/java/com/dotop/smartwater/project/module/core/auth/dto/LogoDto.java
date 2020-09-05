package com.dotop.smartwater.project.module.core.auth.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2019年2月26日
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogoDto extends BaseDto {
	private byte[] content;
	private String name;
	private String stat;
	private String ossurl;
}
