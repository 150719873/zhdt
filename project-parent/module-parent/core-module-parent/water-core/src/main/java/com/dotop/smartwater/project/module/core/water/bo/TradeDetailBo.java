package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易明细
 * 

 * @date 2019年3月9日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TradeDetailBo extends BaseBo {
	/** 主键 */
	private String id;
	/** 订单号 */
	private String number;
	/** 交易流水号 */
	private String tradeNumber;
	/** 创建时间 */
	private String createTime;
	/** 账单金额 */
	private String amount;
	/** 实收金额 */
	private String netReceipts;
	/** 找零金额 */
	private String giveChange;
	/** 支付方式 */
	private String mode;
	/** 支付状态 */
	private String status;
	/** 交易说明 */
	private String explan;
	/** 操作员ID */
	private String operatorId;
	/** 操作员 */
	private String operatorName;
	/** 第三方支付的流水号 */
	private String thirdPartyNum;

	/** IP地址 */
	private String ip;
	/** 刷开支付码 */
	private String paycard;

}
