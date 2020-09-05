package com.dotop.pipe.api.service.report;

import java.util.List;

import com.dotop.pipe.core.vo.report.TimingCalculationVo;
import com.dotop.pipe.data.report.core.bo.report.TimingCalculationBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface ITimingCalculationService extends BaseService<TimingCalculationBo, TimingCalculationVo> {

	/**
	 * 分页
	 */
	public Pagination<TimingCalculationVo> page(TimingCalculationBo timingCalculationBo)
			throws FrameworkRuntimeException;

	/**
	 * 查询
	 */
	public TimingCalculationVo get(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException;

	/**
	 * 新增
	 */
	public TimingCalculationVo add(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException;

	/**
	 * 编辑
	 */
	public TimingCalculationVo edit(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException;

	/**
	 * 删除
	 */
	public String del(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException;

	/**
	 * 通过编码或主键判断是否存在
	 */
	public boolean isExist(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException;

	/**
	 * 列表
	 */
	public List<TimingCalculationVo> list(TimingCalculationBo timingCalculationBo) throws FrameworkRuntimeException;
}
