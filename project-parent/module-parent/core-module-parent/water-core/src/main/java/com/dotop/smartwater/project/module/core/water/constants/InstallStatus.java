package com.dotop.smartwater.project.module.core.water.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 报装管理-流程
 *

 * @date 2019年3月11日
 */
public enum InstallStatus {


	/**
	 * 未完善报装申请
	 */
	NOAPPLY(100, "100", "未完善报装申请", "APPLY"),
	APPLY(101, "101", "已完善报装申请", "APPLY"),
	CHANGE(110, "110", "换表申请", "CHANGE"),

	NOSURVEY(200, "200", "未发起勘测", "SURVEY"),
	SURVEY(210, "210", "已发起勘测", "SURVEY"),
	SURVEYFAIL(220, "220", "勘测未通过", "SURVEY"),
	SURVEYPASS(230, "230", "勘测通过", "SURVEY"),

	NOSIGN(300, "300", "未生成合同", "CONTRACT"),
	SIGN(310, "310", "已生成合同", "CONTRACT"),
	SIGNPASS(320, "320", "合同已签订", "CONTRACT"),

	NOPAY(400, "400", "未发起支付", "AMOUNT"),
	PAY(410, "410", "已发起支付", "AMOUNT"),
	PAYFAIL(420, "420", "支付失败", "AMOUNT"),
	PAYPASS(430, "430", "支付成功", "AMOUNT"),

	NOSHIPMENT(500, "500", "未发起出货", "SHIPMENT"), SHIPMENT(510, "510", "已发起出货", "SHIPMENT"),
	SHIPMENTFAIL(520, "520", "出货失败", "SHIPMENT"), SHIPMENTPASS(530, "530", "出货成功", "SHIPMENT"),

	NOACCEPTANCE(600, "600", "未发起验收", "ACCEPTANCE"), ACCEPTANCE(610, "610", "已发起验收", "ACCEPTANCE"),
	ACCEPTANCEFAIL(620, "620", "验收未通过", "ACCEPTANCE"), ACCEPTANCEPASS(630, "630", "验收通过", "ACCEPTANCE"),

	NOIMPORT(700, "700", "未发起导入", "USER"), IMPORTFAIL(710, "710", "导入失败", "USER"),
	IMPORTPASS(720, "720", "导入成功", "USER"),

	ENDFLOW(800, "800", "结束", ""),

	NOFILE(900, "900", "未发起归档", ""), FILEFAIL(910, "910", "归档失败", ""), FILEPASS(920, "920", "归档成功", "");

	private int intValue;

	private String stringVal;

	private String text;
	private String type;

	InstallStatus(int intValue, String stringVal, String text, String type) {
		this.intValue = intValue;
		this.stringVal = stringVal;
		this.text = text;
		this.type = type;
	}

	public int intValue() {
		return intValue;
	}

	public String getText() {
		return text;
	}

	public String getType() {
		return type;
	}

	public String getStringVal() {
		return stringVal;
	}

	public static String getText(Integer intValue) {
		if (intValue == null) {
			return "";
		}
		for (InstallStatus statuss : InstallStatus.values()) {
			if (statuss.intValue == intValue) {
				return statuss.getText();
			}
		}
		return "";
	}

	public static String getType(Integer intValue) {
		if (intValue == null) {
			return "";
		}

		for (InstallStatus statuss : InstallStatus.values()) {
			if (statuss.intValue == intValue) {
				return statuss.getType();
			}
		}
		return "";
	}

	public static String getType(String value) {
		if (value == null) {
			return "";
		}

		for (InstallStatus statuss : InstallStatus.values()) {
			if (statuss.intValue == Integer.valueOf(value)) {
				return statuss.getType();
			}
		}
		return "";
	}

	public static String getText(String stringVal) {
		if (stringVal == null || "".equals(stringVal)) {
			return "";
		}

		for (InstallStatus statuss : InstallStatus.values()) {
			if (statuss.stringVal.equals(stringVal)) {
				return statuss.getText();
			}
		}
		return "";
	}

	public static String getStringVal(Integer intValue) {
		if (intValue == null) {
			return "";
		}

		for (InstallStatus statuss : InstallStatus.values()) {
			if (statuss.intValue == intValue) {
				return statuss.getStringVal();
			}
		}
		return "";
	}

	public static List<Integer> listKey() {
		List<Integer> list = new ArrayList<>();
		for (InstallStatus enums : InstallStatus.values()) {
			list.add(enums.intValue);
		}
		return list;
	}

	/**
	 * 检查状态是否为环节的最后一个节点
	 *
	 * @param sVal
	 * @return
	 */
	public static boolean checkNode(String sVal) {


		if (sVal == null || "".equals(sVal)) {
			return false;
		}

		String type = "";

		for (InstallStatus enums : InstallStatus.values()) {
			if (enums.intValue == Integer.valueOf(sVal)) {
				type = enums.type;
			}
		}
		boolean temp = true;
		for (InstallStatus enums : InstallStatus.values()) {
			if (enums.type.equals(type) && enums.intValue > Integer.valueOf(sVal)) {
				temp = false;
				break;
			}
		}
		return temp;
	}

}
