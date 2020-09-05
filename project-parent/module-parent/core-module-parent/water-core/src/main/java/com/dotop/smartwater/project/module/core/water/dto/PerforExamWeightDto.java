package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PerforExamWeightDto extends BaseDto{

	/** 主键*/
	private String id;
	/** 批次号*/
	private String number;
	/** 标题*/
	private String title;
	/** 描述*/
	private String describe;
	/** 分数*/
	private String score;
}
