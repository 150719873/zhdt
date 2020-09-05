package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 绩效考核-考核人员权重评分表
 * 

 * @date 2019年2月28日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PerforExamFillVo extends BaseVo {

	/** 主键 */
	private String id;
	/** 批次号 */
	private String number;
	/** 权重ID */
	private String weightId;
	/** 考核对象ID */
	private String assessmentId;
	/** 考核对象名称 */
	private String assessmentName;
	/** 分数 */
	private String score;
	/** 权重标题 */
	private String title;
	/** 权重说明 */
	private String describe;
	/** 权重分数 */
	private String weightScore;

}
