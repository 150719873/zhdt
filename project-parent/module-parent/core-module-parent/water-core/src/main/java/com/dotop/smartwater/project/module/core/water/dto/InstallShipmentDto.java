package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-出货
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallShipmentDto extends BaseDto {
	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 产品型号ID */
	private String modelId;
	/** 产品型号名称 */
	private String modelName;
	/** 出货单号 */
	private String shipNo;
	/** 审核状态 */
	private String status;
	/** 出货数量 */
	private String shipNumber;
	/** 出货时间 */
	private String shipTime;
}
