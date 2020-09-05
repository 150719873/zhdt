package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterFormDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterFormVo;

import java.util.List;

public interface IFormDao extends BaseDao<WorkCenterFormDto, WorkCenterFormVo> {
	/**
	 * 新增
	 */
	@Override
	void add(WorkCenterFormDto formDto);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterFormVo get(WorkCenterFormDto formDto);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterFormVo> list(WorkCenterFormDto formDto);

	/**
	 * 修改
	 */
	@Override
	Integer edit(WorkCenterFormDto formDto);

	/**
	 * 删除
	 */
	@Override
	Integer del(WorkCenterFormDto formDto);

}
