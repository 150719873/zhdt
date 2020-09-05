/**
 * jelibuy-server
 * com.jelibuy.server.wechar.bean
 * OrderQueryResult.java
 * <p>Copyright: Copyright (c) 2015 深圳接力电子商务有限公司</p>
 */
package com.dotop.smartwater.project.module.core.third.bo.wechat;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.core.third.enums.wechat.TradeState;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @createTime 2015年5月14日 下午5:17:09
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WeixinOrderQueryBo extends WeixinPayBaseBo {

	private boolean isSubscribe;

	private TradeState tradeState;

	private String tradeStateDesc;

	private String bankType;

	private int cashFee;

	private String cashFeeType;

	private int couponFee;

	private int couponCount;

	private List<WeixinCouponBo> couponList;

	private String transactionId;

	private Date timeEnd;

}
