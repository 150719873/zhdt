package com.dotop.smartwater.project.module.core.water.enums;

import lombok.Getter;

@Getter
public enum WorkCenterDbLoad {

	LOAD("LOAD", "加载"), NOT_LOAD("NOT_LOAD", "不载入");

	private String key;

	private String val;

	WorkCenterDbLoad(String key, String val) {
		this.key = key;
		this.val = val;
	}

	public static String get(String key) {
		WorkCenterDbLoad[] list = WorkCenterDbLoad.values();
		for (WorkCenterDbLoad obj : list) {
			if (key == obj.key) {
				return obj.val;
			}
		}
		return null;
	}
}
