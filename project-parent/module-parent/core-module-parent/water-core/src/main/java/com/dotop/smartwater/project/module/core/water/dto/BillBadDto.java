package com.dotop.smartwater.project.module.core.water.dto;

import java.math.BigDecimal;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2019年3月11日
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BillBadDto extends BaseDto {

	/**
	 * 坏账id
	 */
	private String billBadId;

	/**
	 * 账单对账id
	 */
	private String billCheckId;

	private String billTitle;

	/**
	 * 账单id
	 */
	private String monthBillId;

	/**
	 * 账单流水号
	 */
	private String monthBillTradeno;

	/**
	 * 是否是坏账 0 是坏账 1 不是
	 */
	private String isBad;

	/**
	 * 月账单金额
	 */
	private BigDecimal monthBillCount;

	private BigDecimal monthBillPenalty;

	private String processId;

	private String processStatus;

}
