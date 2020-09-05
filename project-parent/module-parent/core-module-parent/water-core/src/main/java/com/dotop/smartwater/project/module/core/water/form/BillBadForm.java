package com.dotop.smartwater.project.module.core.water.form;

import java.math.BigDecimal;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2019年3月11日
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BillBadForm extends BaseForm {

	/**
	 * 坏账id
	 */
	private String billBadId;

	private String billTitle;

	/**
	 * 账单对账id
	 */
	private String billCheckId;

	/**
	 * 账单id
	 */
	private String monthBillId;

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

}
