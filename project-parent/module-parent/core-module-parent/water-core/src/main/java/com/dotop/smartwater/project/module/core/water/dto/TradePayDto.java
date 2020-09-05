package com.dotop.smartwater.project.module.core.water.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同trade_pay
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class TradePayDto extends BaseDto {

	private String id;

	private String tradeno;

	private String payno;

	private Date createtime;

	private Double beforemoney;

	private Double amount;

	private Double actualamount;

	private Double aftermoney;

	// 支付方式（1-现金支付 2-微信支付 3-支付宝支付 4-微信刷卡支付）
	private Integer paytype;

	private Integer paystatus;

	private String remark;

	private Date paytime;

	private String userno;

	private String username;

	private String operatorid;

	private String operatorname;

	private Date operatortime;

	private BigDecimal ownerpay;

	private BigDecimal givechange;

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno == null ? null : tradeno.trim();
	}

	public void setPayno(String payno) {
		this.payno = payno == null ? null : payno.trim();
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

}