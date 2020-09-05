package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 绩效考核-考核人员权重评分表

 * @date   2019年2月28日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PerforExamFillDto extends BaseDto{

	/** 主键*/
	private String id;
	/** 批次号*/
	private String number;
	/** 权重ID*/
	private String weightId;
	/** 考核对象ID*/
	private String assessmentId;
	/** 考核对象名称*/
	private String assessmentName;
	/** 分数*/
	private String score;
	
}
