package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 绩效考核-模板（关系表）
 * 

 * @date 2019年2月28日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PerforTemplateRelationVo extends BaseVo {

	/** 主键 */
	private String id;
	/** 模板ID */
	private String templateId;
	/** 权重ID */
	private String weightId;

}
