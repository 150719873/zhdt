package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessDbDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessDbVo;

public interface IProcessDbDao extends BaseDao<WorkCenterProcessDbDto, WorkCenterProcessDbVo> {

	@Override
	void add(WorkCenterProcessDbDto processDbDto);
}
