package com.dotop.pipe.data.report.core.vo;

import java.util.List;

import lombok.Data;

@Data
public class ReportGroupVo {

	private String deviceId;
	private List<ReportVo> reports;

	public ReportGroupVo() {
		super();
	}

	public ReportGroupVo(String deviceId, List<ReportVo> reports) {
		super();
		this.deviceId = deviceId;
		this.reports = reports;
	}

}
