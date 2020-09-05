package com.dotop.pipe.data.report.core.vo;

import java.util.List;

import lombok.Data;

@Data
public class ReportAreaGroupVo {

	private String areaId;
	private String areaName;
	private String areaCode;

	private List<ReportAreaVo> reportAreas;

}
