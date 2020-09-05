package com.dotop.pipe.data.report.core.vo;

import lombok.Data;

/**
 *
 * @date 2018/11/14.
 */
@Data
public class ReportAreaVo {
	private String areaId;
	private String areaName;
	private String areaCode;
	private String parentCode;
	private String totalVal;
	private String sendYear;
	private String sendMonth;
	private String sendDay;
	private String sendHour;
}
