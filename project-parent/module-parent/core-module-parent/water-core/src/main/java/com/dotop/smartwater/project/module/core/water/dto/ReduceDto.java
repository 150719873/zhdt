package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同reduce
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ReduceDto extends BaseDto {
	private String reduceid;
	private String name;
	private Integer unit;
	private Double rvalue;
	private Date ctime;
	private String cuser;

}