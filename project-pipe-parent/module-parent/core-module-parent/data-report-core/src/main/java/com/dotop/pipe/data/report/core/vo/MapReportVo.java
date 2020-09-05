package com.dotop.pipe.data.report.core.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MapReportVo extends BaseVo {

	// 传感器主键
	private String sensorId;

	// 字段
	private String field;

	// 值
	private String val;

}
