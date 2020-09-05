package com.dotop.smartwater.project.module.core.demo.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HelloDto extends BaseDto {

	private String name;
}
