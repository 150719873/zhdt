package com.dotop.pipe.core.vo.report;

import java.math.BigDecimal;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimingCalculationVo extends BasePipeVo {

	private String tcId;

	private String code;

	private String name;

	private String des;

	private String status;

	private String upperLimit;

	private String lowerLimit;

	private BigDecimal result; // 结果

	private Boolean isNormal = true; // 状态是否正常 true ： 正常 false： 异常

	private List<TimingFormulaVo> formulas;
}
