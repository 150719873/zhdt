package com.dotop.smartwater.project.module.core.water.form;

import java.math.BigDecimal;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.form.customize.LadderPriceDetailForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO 与OrderExtVo合并
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderExtForm extends BaseVo {

	private String tradeno;

	private String couponinfo;

	private String reduceinfo;

	private String purposeinfo;

	private Double penalty;

	private Double alreadypay;

	private Integer ischargebacks;

	// 收费种类
	private String chargeinfo;

	private String cardid;

	private String paytypeinfo;

	// -------------------------加入orderExtVo内容
	// 实现兼容------------------------------------------

	/* 账单ID */
	private String id;

	/** 账单ID（多个） */
	private String orderids;

	/* 实缴金额 */
	private Double realamount;

	/* 余额抵扣 */
	private Double balance;

	/* 水表用途 */
	private PurposeForm purpose;

	/* 减免类型 */
	private ReduceForm reduce;

	private TradePayForm tradepay;

	/* 选择的优惠券Id */
	private String couponid;

	/* 减免类型 */
	private List<CouponForm> couponList;

	private BigDecimal ownerpay;

	/* 支付二维码值 */
	private String paycard;

	/* 支付方式 */
	private String payMode;

	/* 剩余应缴 */
	private Double surpluspay;

	/* 前端请求IP地址 */
	private String ip;

	private String ownerid;

	/* 水费组成明细 */
	private List<LadderPriceDetailForm> payDetailList;

	private OrderForm order;

}