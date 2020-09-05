package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InstallAppointmentFunctionVo extends BaseVo {
	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 功能ID */
	private String functionId;
	/** 模板ID */
	private String templateId;
	/** 表名 */
	private String tableName;
	/** 功能名称 */
	private String name;
	/** 是否主体（1-是 0-否） */
	private String isHead;
	/** 对应前端页面 */
	private String pageUrl;
}
