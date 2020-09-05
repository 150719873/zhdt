package com.dotop.pipe.core.vo.report;

import java.math.BigDecimal;
import java.util.List;

import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LeakageAnalysisVo extends BasePipeVo {

	private BigDecimal result; // 结果

	// 规则详情
	private TimingCalculationVo timingCalculationVo;

	private List<DeviceVo> devices; // 设备集合

	private List<LeakageAnalysisItemVo> items; // 观察点集合
}
