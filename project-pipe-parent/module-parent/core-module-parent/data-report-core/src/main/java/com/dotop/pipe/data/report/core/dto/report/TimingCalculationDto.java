package com.dotop.pipe.data.report.core.dto.report;

import com.dotop.pipe.data.report.core.enums.TimingStatusEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimingCalculationDto extends BasePipeDto {

	private String tcId;

	private String code;

	private String name;

	private String des;

	private String upperLimit;

	private String lowerLimit;

	private TimingStatusEnum status;

}
