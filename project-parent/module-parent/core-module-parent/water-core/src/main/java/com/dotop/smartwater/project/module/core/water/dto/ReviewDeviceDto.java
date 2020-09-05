package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备复核列表
 * 

 * @date 2019年4月6日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReviewDeviceDto extends BaseDto {
	/** 主键 */
	private String id;
	/** 批次号 */
	private String batchNo;
	/** 标题 */
	private String title;
	/** 区域IDs */
	private String communityIds;
	/** 区域名称 */
	private String communityNames;
	/** 复核人员名称 */
	private String userNames;
	/** 复核人员IDs */
	private String userIds;
	/** 复核人员数量 */
	private String number;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/** 状态（1-未开始 2-处理中 3-已完成 4-已结束） */
	private String status;
	/** 允许差值 */
	private String diff;
	/** 复核设备总数 */
	private String devNumber;
}
