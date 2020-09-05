package com.dotop.smartwater.project.module.core.water.form.customize;

import java.math.BigDecimal;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderPayParamForm extends BaseForm {

	private String id;
	/* 账单流水号 */
	private String tradeno;
	/* 余额抵扣 */
	private Double balance;

	private Double alreadypay;

	private Double realamount;

	private BigDecimal ownerpay;

	private String couponid;

	private Double penalty;

	private String orderids;

	private String ip;

	private Integer paytype;

	private String payMode;
	
	private String[] tradenos;// 交易流水号
}
