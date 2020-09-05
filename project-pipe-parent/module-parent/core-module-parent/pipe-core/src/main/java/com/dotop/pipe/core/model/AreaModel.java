package com.dotop.pipe.core.model;

import com.dotop.pipe.auth.core.model.BaseEnterpriseModel;
import com.dotop.pipe.core.constants.PipeConstants;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 区域 
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaModel extends BaseEnterpriseModel {

	// 主键
	private String areaId;

	// 区域编号
	private String areaCode;

	// 区域名字
	private String name;

	// 区域描述
	private String des;

	// 是否根节点(0:非叶子;1:非叶子)
	private Integer isLeaf = PipeConstants.AREA_IS_LEAF;

	// 父节点
	private String parentCode = PipeConstants.AREA_PARENT_CODE;

	// 是否父节点
	private Integer is_parent;
}
