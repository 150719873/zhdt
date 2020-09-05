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
public class BillCheckBo extends BaseBo {
	/**
	 * 账单对账
	 */
	private String billCheckId;

	/**
	 * 流水号
	 */
	private String serialNumber;

	/**
	 * 对账标题
	 */
	private String billTitle;

	/**
	 * 账单状态
	 */
	private String billStatus;

	/**
	 * 流程状态
	 */
	private String processStatus;
	
	/**
	 * 已缴_原始金额
	 */
	private BigDecimal originalPaid;
	
	/**
	 * 未缴账单_原始金额
	 */
	private BigDecimal originalNotPaid;
	
	/**
	 * 未缴账单_减免后金额
	 */
	private BigDecimal amountNotPaid;
	
	/**
	 * 已缴账单_减免后金额
	 */
	private BigDecimal amountPaid;
	
	/**
	 * 减免 金额
	 * 
	 */
	private BigDecimal reduce;
	
	/**
	 *已缴账单 - 逾期金额
	 * 
	 */
	private BigDecimal penaltyPaid;
	
	/**
	 * 未缴账单-- 逾期金额
	 * 
	 */
	private BigDecimal penaltyNotPaid;
	
	/**
	 * 现金支付
	 * 
	 */
	private BigDecimal cash;
	
	/**
	 * 微信
	 * 
	 */
	private BigDecimal wechat;
	
	/**
	 * 微信刷卡
	 * 
	 */
	private BigDecimal wechatCard;
	
	/**
	 * 支付宝
	 * 
	 */
	private BigDecimal alipay;
	
	/**
	 * 代金券
	 * 
	 */
	private BigDecimal voucher;
	
	
	/**
	 * 抵扣券
	 * 
	 */
	private BigDecimal coupon;
	

	/**
	 * 流程id
	 */
	private String processId;

	/**
	 * 对账开始时间
	 */
	private String startDate;

	/**
	 * 对账结束时间
	 */
	private String endDate;
	
	/**
	 *  流程申请人
	 */
	private String processCreateBy;
}
