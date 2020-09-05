package com.dotop.pipe.web.api.factory.report;

import java.util.List;

import com.dotop.pipe.core.vo.report.TimingCalculationVo;
import com.dotop.pipe.data.report.core.form.TimingCalculationForm;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface ITimingCalculationFactory extends BaseFactory<TimingCalculationForm, TimingCalculationVo> {

	public Pagination<TimingCalculationVo> page(TimingCalculationForm timingCalculationForm)
			throws FrameworkRuntimeException;

	public TimingCalculationVo get(TimingCalculationForm timingCalculationForm) throws FrameworkRuntimeException;

	public TimingCalculationVo add(TimingCalculationForm timingCalculationForm) throws FrameworkRuntimeException;

	public TimingCalculationVo edit(TimingCalculationForm timingCalculationForm) throws FrameworkRuntimeException;

	public String del(TimingCalculationForm timingCalculationForm) throws FrameworkRuntimeException;

	public List<TimingCalculationVo> list(TimingCalculationForm timingCalculationForm) throws FrameworkRuntimeException;
}
