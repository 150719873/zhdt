package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同reduce
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ReduceVo extends BaseVo {
	private String reduceid;
	private String name;
	private Integer unit;
	private Double rvalue;
	private String ctime;

}