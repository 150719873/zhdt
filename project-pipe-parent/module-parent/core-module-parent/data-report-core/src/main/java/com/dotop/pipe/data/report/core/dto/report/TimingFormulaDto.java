package com.dotop.pipe.data.report.core.dto.report;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimingFormulaDto extends BasePipeDto {

	private String tfId;

	private String deviceId;

	private String direction;

	private String multiple;

	private String tcId;

}
