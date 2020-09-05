package com.dotop.pipe.data.report.core.dto.report;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MapReportDto extends BaseDto {

	private String[] sensorIds;

	private String sensorType;

}
