package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备复核详情
 * 

 * @date 2019年4月6日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReviewDetailDto extends BaseDto {
	/***/
	private String id;
	/** 用户编号、水表号、电话 -关键字 */
	private String keyWord;
	/** 批次号 */
	private String batchNo;
	/** 区域ID */
	private String communityId;
	/** 区域名称 */
	private String communityName;
	/** 用户编号 */
	private String userNo;
	/** 用户名称 */
	private String userName;
	/** 手机号 */
	private String userPhone;
	/** 地址 */
	private String userAddr;
	/** 设备号 */
	private String devno;
	/** 最近上报读数 */
	private String water;
	/** 最近上报时间 */
	private String uplinkTime;
	/** 复核读数 */
	private String reviewWater;
	/** 复核时间 */
	private String reviewTime;
	/** 复核状态 */
	private String reviewStatus;
	/** 提交状态 */
	private String submitStatus;
	/** 说明 */
	private String reviewExplan;
}
