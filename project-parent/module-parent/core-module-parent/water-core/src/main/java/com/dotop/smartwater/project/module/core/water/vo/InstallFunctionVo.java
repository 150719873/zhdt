package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-功能
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallFunctionVo extends BaseVo {
	/** 主键 */
	private String id;
	/** 表名 */
	private String tableName;
	/** 功能名称 */
	private String name;
	/** 是否主体（1-是 0-否） */
	private String isHead;
	/** 对应前端页面 */
	private String pageUrl;
}
