package com.dotop.pipe.web.api.factory.report;

import java.util.List;

import com.dotop.pipe.core.form.LeakageAnalysisForm;
import com.dotop.pipe.core.vo.report.LeakageAnalysisVo;
import com.dotop.pipe.core.vo.report.TimingCalculationVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface ILeakageAnalysisFactory extends BaseFactory<LeakageAnalysisForm, LeakageAnalysisVo> {

	public LeakageAnalysisVo calculation(LeakageAnalysisForm leakageAnalysisForm) throws FrameworkRuntimeException;

	// public List<LeakageAnalysisVo> timing1(LeakageAnalysisForm
	// leakageAnalysisForm) throws FrameworkRuntimeException;

	public List<TimingCalculationVo> timing(LeakageAnalysisForm leakageAnalysisForm) throws FrameworkRuntimeException;
}
