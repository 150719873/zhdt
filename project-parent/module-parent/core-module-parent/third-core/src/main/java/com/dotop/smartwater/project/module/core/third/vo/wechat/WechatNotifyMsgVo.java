package com.dotop.smartwater.project.module.core.third.vo.wechat;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2018年7月17日 上午11:20:08
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatNotifyMsgVo {

	private String id;

	private Date createtime;

	private String appid;

	private String mchId;

	private String deviceInfo;

	private String nonceStr;

	private String sign;

	private String signType;

	private String resultCode;

	private String errCode;

	private String errCodeDes;

	private String openid;

	private String isSubscribe;

	private String tradeType;

	private String bankType;

	private Integer totalFee;

	private Integer settlementTotalFee;

	private String feeType;

	private String cashFee;

	private String cashFeeType;

	private Integer couponFee;

	private Integer couponCount;

	private String couponType;

	private String couponId;

	private Integer couponFeeSingle;

	private String transactionId;

	private String outTradeNo;

	private String attach;

	private String timeEnd;

	private String returnCode;

	private String notifyMsg;

	private String returnMsg;
}
