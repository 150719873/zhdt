package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessFormDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessFormVo;

public interface IProcessFormDao extends BaseDao<WorkCenterProcessFormDto, WorkCenterProcessFormVo> {

	@Override
	void add(WorkCenterProcessFormDto processFormDto);

}
