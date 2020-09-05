package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-费用
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallAmountVo extends BaseVo {

	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 设备数量 */
	private String deviceNumbers;
	/** 产品型号ID */
	private String modelId;
	/** 产品型号名称 */
	private String modelName;
	/** 单价 */
	private String price;
	/** 其他费用 */
	private String otherExpenses;
	/** 总计 */
	private String amount;
	/** 支付方式 */
	private String payType;
	/** 实收 */
	private String netReceipts;
	/** 找零 */
	private String giveChange;

	/** 交易流水号（内部） */
	private String tradeNo;
	/** 交易状态 */
	private String tradeStatus;
	/** 交易时间 */
	private String tradeDate;
	/** 交易流水 */
	private TradeOrderVo order;

}
