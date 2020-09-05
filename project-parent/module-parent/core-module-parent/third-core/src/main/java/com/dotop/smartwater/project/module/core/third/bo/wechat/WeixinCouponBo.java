/**
 * jelibuy-server
 * com.jelibuy.server.bean
 * WeixinCoupon.java
 * <p>Copyright: Copyright (c) 2015 深圳接力电子商务有限公司</p>
 */
package com.dotop.smartwater.project.module.core.third.bo.wechat;

import lombok.Data;

/**

 * @createTime 2015年4月14日 下午5:05:29
 * @since 1.0
 */
@Data
public class WeixinCouponBo {

	private String tradeSN;

	private String transactionId;

	private String couponBatchId;

	private String couponId;

	private int couponFee;

	public WeixinCouponBo() {

	}

	public WeixinCouponBo(String tradeSN, String transactionId, String couponBatchId, String couponId, int couponFee) {
		this.tradeSN = tradeSN;
		this.transactionId = transactionId;
		this.couponBatchId = couponBatchId;
		this.couponId = couponId;
		this.couponFee = couponFee;
	}

}
