package com.dotop.pipe.data.report.core.vo;

import lombok.Data;

@Data
public class SensorReportVo {

	private String field;
	private String totalVal;
	private String minVal;
	private String maxVal;
	private String avgVal;
	private String countTime;
	private String type;

	private String sensorId;
	private String sensorCode;
	private String sensorName;
	private String sensorVersion;
	private String sensorFactory;

	private String areaName;
	
	// 水质计添加字段
	private String qualityPh;
	private String qualityOxygen;
	private String qualityChlorine;
	private String qualityTurbid;
	private String qualityTemOne;
	private String qualityTemTwo;
	private String qualityTemThree;
	private String qualityTemFour;
}
