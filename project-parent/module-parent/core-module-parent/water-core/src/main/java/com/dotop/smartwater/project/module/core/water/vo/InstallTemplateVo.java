package com.dotop.smartwater.project.module.core.water.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-模板
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallTemplateVo extends BaseVo {
	/** 主键 */
	private String id;
	/** 编号 */
	private String no;
	/** 模板名称 */
	private String name;
	/** 业务类型 */
	private String type;
	/** 描述 */
	private String describe;
	/** 节点数量 */
	private String nodes;
	/** 功能列表 */
	private List<InstallTemplateRelationVo> relations;
}
