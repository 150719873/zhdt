package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/7/25.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterTmplNodePointDto extends BaseDto{
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 颜色
	 */
	private String color;

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
	 * 尺寸
	 */
	private String size;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 横坐标
	 */
	private Integer x;

	/**
	 * 纵坐标
	 */
	private Integer y;

	/**
	 * 关联模板id
	 */
	private String tmplId;

	/**
	 * 关联节点id
	 */
	private String nodeId;

}
