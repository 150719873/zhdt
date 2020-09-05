package com.dotop.smartwater.project.module.core.auth.dto;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserAreaDto extends BaseDto {
	public String userid;

	public List<String> areaids;
}
