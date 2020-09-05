package com.dotop.smartwater.project.module.core.water.bo;

import java.math.BigDecimal;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2019年3月11日
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BillBadBo extends BaseBo {

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
	private String monthBillCount;
	
    private BigDecimal monthBillPenalty;
	
	private String processId;
	
	private String processStatus;

}
