package com.dotop.pipe.data.report.core.vo;

import java.util.List;

import lombok.Data;

@Data
public class AreaReportVo {

	private String field;
	private String totalVal;
	private String countTime;
	private String type;
	private String areaName;
	private String areaDes;
	private String areaCode;
	private String createBy;
	private String areaId;
	private Integer areaSumCount;
	private Integer areaDealCount;
	private Double flwTotalValue;
	public List<ReportVo> deviceList;
}
