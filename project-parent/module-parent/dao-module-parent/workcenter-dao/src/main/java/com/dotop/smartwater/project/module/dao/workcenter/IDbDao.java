package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterDbDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbVo;

import java.util.List;

public interface IDbDao extends BaseDao<WorkCenterDbDto, WorkCenterDbVo> {

	/**
	 * 新增
	 */
	@Override
	void add(WorkCenterDbDto workCenterDbDto);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterDbVo get(WorkCenterDbDto workCenterDbDto);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterDbVo> list(WorkCenterDbDto workCenterDbDto);

	/**
	 * 修改
	 */
	@Override
	Integer edit(WorkCenterDbDto workCenterDbDto);

	/**
	 * 删除
	 */
	@Override
	Integer del(WorkCenterDbDto workCenterDbDto);

	/**
	 * 批量新增
	 */
	@Override
	void adds(List<WorkCenterDbDto> ds);

	/**
	 * 批量删除
	 */
	@Override
	void dels(List<WorkCenterDbDto> ds);
}
