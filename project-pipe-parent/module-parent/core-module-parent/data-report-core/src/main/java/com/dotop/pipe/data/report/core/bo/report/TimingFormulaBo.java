package com.dotop.pipe.data.report.core.bo.report;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimingFormulaBo extends BasePipeBo {

	private String tfId;

	private String deviceId;

	private String direction;

	private String multiple;

	private String tcId;

}
