package com.dotop.pipe.core.enums;

import com.dotop.pipe.core.constants.DateTypeConstants;

/**
 *
 * @date 2018/11/2.
 */
public enum DateTypeEnum {

	/**
	 * 区间
	 */
	RANGE("区间", "RANGE"),
	/**
	 * 实时统计
	 */
	REAL_TIME("实时", DateTypeConstants.REAL_TIME),
	/**
	 * 时统计
	 */
	HOUR("时", DateTypeConstants.HOUR),
	/**
	 * 日统计
	 */
	DAY("日", DateTypeConstants.DAY),
	/**
	 * 月统计
	 */
	MONTH("月", DateTypeConstants.MONTH),

	/**
	 * 年统计
	 */
	YEAR("年", DateTypeConstants.YEAR),

	/**
	 * 其他类型
	 */
	OTHER("其他", DateTypeConstants.OTHER);

	private String name;
	private String code;

	DateTypeEnum(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.code;
	}
}
