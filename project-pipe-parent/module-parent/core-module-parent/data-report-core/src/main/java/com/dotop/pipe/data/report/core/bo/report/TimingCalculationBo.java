package com.dotop.pipe.data.report.core.bo.report;

import com.dotop.pipe.data.report.core.enums.TimingStatusEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimingCalculationBo extends BasePipeBo {

	private String tcId;

	private String code;

	private String name;

	private String des;

	private String upperLimit;

	private String lowerLimit;

	private TimingStatusEnum status;

}
