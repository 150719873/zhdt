package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易订单
 * 

 * @date 2019年3月9日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TradeOrderDto extends BaseDto {
	/** 主键 */
	private String id;
	/** 订单号 */
	private String number;
	/** 交易流水号 */
	private String tradeNumber;
	/** 交易名称 */
	private String tradeName;
	/** 缴费人ID */
	private String userId;
	/** 缴费人姓名 */
	private String userName;
	/** 缴费人电话 */
	private String userPhone;
	/** 缴费金额 */
	private String amount;
	/** 缴费方式 */
	private String mode;
	/** 支付状态 */
	private String status;
	/** 支付时间 */
	private String payTime;
	/** 订单来源 */
	private String type;
}
