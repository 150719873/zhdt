package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterTmplNodeDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeVo;

import java.util.List;

public interface ITmplNodeDao extends BaseDao<WorkCenterTmplNodeDto, WorkCenterTmplNodeVo> {
	/**
	 * 新增
	 */
	@Override
	void add(WorkCenterTmplNodeDto tmplNodeDto);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterTmplNodeVo get(WorkCenterTmplNodeDto tmplNodeDto);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterTmplNodeVo> list(WorkCenterTmplNodeDto tmplNodeDto);

	/**
	 * 修改
	 */
	@Override
	Integer edit(WorkCenterTmplNodeDto tmplNodeDto);

	/**
	 * 删除
	 */
	@Override
	Integer del(WorkCenterTmplNodeDto tmplNodeDto);

	/**
	 * 批量新增
	 */
	@Override
	void adds(List<WorkCenterTmplNodeDto> tmplNodeDtos);

	/**
	 * 批量修改
	 */
	@Override
	void edits(List<WorkCenterTmplNodeDto> tmplNodeDtos);

	/**
	 * 批量删除
	 */
	@Override
	void dels(List<WorkCenterTmplNodeDto> tmplNodeDtos);
}
