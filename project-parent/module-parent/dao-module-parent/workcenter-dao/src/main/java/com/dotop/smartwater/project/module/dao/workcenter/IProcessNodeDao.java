package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessNodeDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessNodeVo;

public interface IProcessNodeDao extends BaseDao<WorkCenterProcessNodeDto, WorkCenterProcessNodeVo> {
	/**
	 * 新增
	 */
	@Override
	void add(WorkCenterProcessNodeDto processNodeDto);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterProcessNodeVo get(WorkCenterProcessNodeDto processNodeDto);

}
