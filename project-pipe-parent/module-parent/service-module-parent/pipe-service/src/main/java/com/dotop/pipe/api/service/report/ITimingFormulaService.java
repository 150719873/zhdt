package com.dotop.pipe.api.service.report;

import java.util.List;

import com.dotop.pipe.core.vo.report.TimingFormulaVo;
import com.dotop.pipe.data.report.core.bo.report.TimingFormulaBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface ITimingFormulaService extends BaseService<TimingFormulaBo, TimingFormulaVo> {

	/**
	 * 新增
	 */
	public TimingFormulaVo add(TimingFormulaBo timingFormulaBo) throws FrameworkRuntimeException;

	/**
	 * 编辑
	 */
	public TimingFormulaVo edit(TimingFormulaBo timingFormulaBo) throws FrameworkRuntimeException;

	/**
	 * 删除
	 */
	public String del(TimingFormulaBo timingFormulaBo) throws FrameworkRuntimeException;

	/**
	 * 通过编码或主键判断是否存在
	 */
	public boolean isExist(TimingFormulaBo timingFormulaBo) throws FrameworkRuntimeException;

	/**
	 * 列表查询
	 */
	public List<TimingFormulaVo> list(TimingFormulaBo timingFormulaBo) throws FrameworkRuntimeException;
}
