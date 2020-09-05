/**
 * jelibuy-server
 * com.jelibuy.server.constant
 * TradeType.java
 * <p>Copyright: Copyright (c) 2015 深圳接力电子商务有限公司</p>
 */
package com.dotop.smartwater.project.module.core.third.enums.wechat;

/**

 * @createTime 2015年4月13日 下午5:34:56
 * @since 1.0
 */
public enum WeixinTradeType {
	
	JSAPI(1),
	NATIVE(2),
	APP(3);
	
	int intValue;
	
	WeixinTradeType(int intValue) {
		this.intValue = intValue;
	}
	
	public int intValue() {
		return intValue;
	}

}
