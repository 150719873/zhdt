/**
 * jelibuy-server
 * com.jelibuy.server.bean
 * WeixinPayBase.java
 * <p>Copyright: Copyright (c) 2015 深圳接力电子商务有限公司</p>
 */
package com.dotop.smartwater.project.module.core.third.bo.wechat;

import com.dotop.smartwater.project.module.core.third.enums.wechat.WeixinTradeType;

import lombok.Data;

/**
 * 微信支付公共参数
 * 

 * @createTime 2015年4月14日 下午5:00:52
 * @since 1.0
 */
@Data
public class WeixinPayBaseBo {

	protected String returnCode;

	protected String returnMsg;

	protected String errCode;

	protected String errCodeDes;

	protected String resultCode;

	protected String appId;

	protected String mchId;

	protected String deviceInfo;

	protected String nonceStr;

	protected String sign;

	protected String openid;

	protected WeixinTradeType tradeType;

	protected int totalFee;

	protected String feeType;

	protected String tradeNO;

	protected String attach;

}
