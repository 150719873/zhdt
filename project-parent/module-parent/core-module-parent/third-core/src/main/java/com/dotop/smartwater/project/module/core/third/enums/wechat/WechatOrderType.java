package com.dotop.smartwater.project.module.core.third.enums.wechat;

/**
 * 

 * @date 2018年7月26日 下午2:40:11
 * @version 1.0.0
 */
public enum WechatOrderType {

	pay_cost(1, "缴费"), 
	recharge(2, "充值");

	private int intValue;

	private String text;

	WechatOrderType(int intValue, String text) {
		this.intValue = intValue;
		this.text = text;
	}

	public int intValue() {
		return intValue;
	}

	public String getText() {
		return text;
	}

	public static String getText(int intValue) {
		for (WechatOrderType enums : WechatOrderType.values()) {
			if (enums.intValue == intValue) {
				return enums.getText();
			}
		}
		return "";
	}
}
