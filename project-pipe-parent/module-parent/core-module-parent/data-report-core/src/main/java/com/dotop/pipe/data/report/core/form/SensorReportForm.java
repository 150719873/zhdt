package com.dotop.pipe.data.report.core.form;

import java.util.Date;

import lombok.Data;

@Deprecated
@Data
public class SensorReportForm {

	// 传感器编号
	private String sensorCode;
	// 传感器类型
	private String sensorType;
	// 开始时间
	private Date startDate;
	// 结束时间
	private Date endDate;

	private String[] positiveValues;
	private String[] reverseValues;

}
