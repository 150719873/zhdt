package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-预约模板功能关系表
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallAppointmentRelationDto extends BaseDto {
	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 模板ID */
	private String templateId;
	/** 功能ID */
	private String functionId;
	/** 序号 */
	private String no;
	/** 说明 */
	private String explain;
	/** 上传模板 */
	private String uploadFile;
}
