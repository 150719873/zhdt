package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterTmplNodeEdgeVo extends BaseVo {

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 下标
	 */
	private Integer index;

	/**
	 * 名称
	 */
	private String label;

	/**
	 * 形状
	 */
	private String shape;

	/**
	 * 颜色
	 */
	private String color;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 源节点id
	 */
	private String source;

	/**
	 * 源节点锚点
	 */
	private Integer sourceAnchor;

	/**
	 * 目标节点id
	 */
	private String target;

	/**
	 * 目标节点锚点
	 */
	private Integer targetAnchor;

	/**
	 * 关联模板id
	 */
	private String tmplId;
}
