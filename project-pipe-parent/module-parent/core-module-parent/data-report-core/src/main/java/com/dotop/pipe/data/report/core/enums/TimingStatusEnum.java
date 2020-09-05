package com.dotop.pipe.data.report.core.enums;

import com.dotop.pipe.data.report.core.constants.ReportConstants;

public enum TimingStatusEnum {

	USE("在用", ReportConstants.TIMING_CALCULATION_STATUS_USE),

	NOUSE("停用", ReportConstants.TIMING_CALCULATION_STATUS_NOUSE);

	private String name;
	private String code;

	TimingStatusEnum(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

}
