package com.dotop.smartwater.project.module.core.third.enums.wechat;

/**
 * 

 * @date 2018年7月26日 下午2:40:11
 * @version 1.0.0
 */
public enum WechatOrderState {

	state_normal(0, "正常"), 
	state_unusual(1, "异常");

	private int intValue;

	private String text;

	WechatOrderState(int intValue, String text) {
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
		for (WechatOrderState enums : WechatOrderState.values()) {
			if (enums.intValue == intValue) {
				return enums.getText();
			}
		}
		return "";
	}
}
