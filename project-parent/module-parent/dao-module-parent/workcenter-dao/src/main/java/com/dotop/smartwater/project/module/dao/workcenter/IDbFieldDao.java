package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterDbFieldDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbFieldVo;

import java.util.List;

public interface IDbFieldDao extends BaseDao<WorkCenterDbFieldDto, WorkCenterDbFieldVo> {

	/**
	 * 新增
	 */
	@Override
	void add(WorkCenterDbFieldDto dbFieldDto);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterDbFieldVo get(WorkCenterDbFieldDto dbFieldDto);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterDbFieldVo> list(WorkCenterDbFieldDto dbFieldDto);

	/**
	 * 修改
	 */
	@Override
	Integer edit(WorkCenterDbFieldDto dbFieldDto);

	/**
	 * 删除
	 */
	@Override
	Integer del(WorkCenterDbFieldDto dbFieldDto);

	/**
	 * 批量新增
	 */
	@Override
	void adds(List<WorkCenterDbFieldDto> dbFieldDtos);

	/**
	 * 批量修改
	 */
	@Override
	void edits(List<WorkCenterDbFieldDto> dbFieldDtos);

	/**
	 * 批量删除
	 */
	@Override
	void dels(List<WorkCenterDbFieldDto> dbFieldDtos);
}
