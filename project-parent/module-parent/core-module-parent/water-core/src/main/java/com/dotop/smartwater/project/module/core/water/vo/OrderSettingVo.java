package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2018/11/20.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderSettingVo extends BaseVo {
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 名称
	 */
	private String settingName;

	/**
	 * 键
	 */
	private String settingKey;
	/**
	 * 值
	 */
	private String settingValue;
	/**
	 * 父ID
	 */
	private String parentId;

	/**
	 * 系统ID：1营收系统，2管漏系统
	 */
	private String systemId;

	/**
	 * 类型：1报装，2报修，3巡检
	 */
	private String type;
	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * 最后修改人
	 */
	private String lastUpdateUser;

	/**
	 * 企业ID
	 */
	// private Long enterpriseid;

	private TreeNode<OrderSettingVo> treeOs;
}
