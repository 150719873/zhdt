package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-预约功能表
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallAppointmentFunctionDto extends BaseDto {

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
