/**
 * jelibuy-server
 * com.jelibuy.server.constant
 * TradeState.java
 * <p>Copyright: Copyright (c) 2015 深圳接力电子商务有限公司</p>
 */
package com.dotop.smartwater.project.module.core.third.enums.wechat;

/**
 * 订单的支付状态

 * @createTime 2015年5月14日 下午5:19:59
 * @since 1.0
 */
public enum TradeState {

	/** 支付成功 **/
	SUCCESS(1),
	/** 转入退款 **/
	REFUND(2),
	/** 未支付 **/
	NOTPAY(3),
	/** 已关闭 **/
	CLOSED(4),
	/** 已撤销 **/
	REVOKED(5),
	/** 用户支付中 **/
	USERPAYING(6),
	/** 支付失败(其他原因，如银行返回失败) **/
	PAYERROR(7);

	private int value;

	TradeState(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}

}
