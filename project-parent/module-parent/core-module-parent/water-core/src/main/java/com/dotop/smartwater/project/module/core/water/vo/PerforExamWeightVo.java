package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 绩效考核-考核权重表

 * @date   2019年2月28日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PerforExamWeightVo extends BaseVo{

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
