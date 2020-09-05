package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 绩效考核-权重

 * @date   2019年2月26日
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class PerforWeightVo extends BaseVo{

	/** 主键*/
	private String id;
	/** 标题*/
	private String title;
	/** 描述*/
	private String describe;
	/** 分数*/
	private String score;
	
}
