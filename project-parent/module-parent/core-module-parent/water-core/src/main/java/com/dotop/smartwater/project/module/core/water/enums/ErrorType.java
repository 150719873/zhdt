package com.dotop.smartwater.project.module.core.water.enums;

/**
 * 账单异常状态申明

 *
 */
/**
 * TODO 迁移water-core 常量
 * 

 *
 */
public enum ErrorType {

	SUCCESS(0, "正常"), NOWATERDATA(1, "没有抄表数据"), OFFLINEDEVICE(2, "设备离线"), WATEREXCEPTION(3,
			"上期读数大于本期读数"), NOSETPAYTYPE(4, "未设置收费种类"),
	NOSETLADDER(5, "未设置收费种类的收费阶梯"), NOINITWATER(6, "水表没有初始读数")
	, NOBIND(7, "该业主绑定的不是总表"), NOACTIVE(8, "设备未激活，不计算水费");

	private int val;

	private String text;

	ErrorType(int val, String text) {
		this.val = val;
		this.text = text;
	}

	public int intValue() {
		return this.val;
	}

	public String text() {
		return this.text;
	}

	public static String getCodeText(int code) {
		ErrorType[] list = ErrorType.values();

		for (ErrorType et : list) {
			if (code == et.val) {
				return et.text();
			}
		}
		return null;
	}

}
