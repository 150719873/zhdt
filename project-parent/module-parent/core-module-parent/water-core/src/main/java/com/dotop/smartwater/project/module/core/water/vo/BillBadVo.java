package com.dotop.smartwater.project.module.core.water.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BillBadVo extends BaseVo {

	/**
	 * 坏账id
	 */
	private String billBadId;

	/**
	 * 账单对账id
	 */
	private String billCheckId;

	/**
	 * 账单id
	 */
	private String monthBillId;

	/**
	 * 账单支付状态
	 */

	private Integer paystatus;

	private String ownerid;

	private String pricetypeid;

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

	/* 区域名称 */
	private String communityname;

	/* 用户编号 */
	private String userno;
	/* 用户名称 */
	private String username;
	/* 手机号 */
	private String phone;
	/* 用水量 */
	private Double water;

	/* 应缴金额 */
	private Double amount;

	/* 账单生成日 */
	private Date generatetime;

	/**
	 * 用户地址
	 */
	private String addr;

	private String month;

	private String serialNumber;
	private String billTitle;
	private String startDate;
	private String endDate;
	private Integer badCount;
	private BigDecimal sumCount;
}
