package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-验收
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallAcceptanceDto extends BaseDto {

	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 验收人ID */
	private String acceptId;
	/** 验收人姓名 */
	private String acceptName;
	/** 验收人截止时间 */
	private String endTime;
	/** 上报地址 */
	private String place;
	/** 验收时间 */
	private String acceptTime;
	/** 验收状态 */
	private String status;
	/** 提交状态 */
	private String submitStatus;
	/** 验收地址 */
	private String addr;
	/** 说明 */
	private String explan;
	/** 附件 */
	private String uploadFile;

}
