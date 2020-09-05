package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessDbFieldDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessDbFieldVo;

public interface IProcessDbFieldDao extends BaseDao<WorkCenterProcessDbFieldDto, WorkCenterProcessDbFieldVo> {

	@Override
	void add(WorkCenterProcessDbFieldDto processDbFieldDto);
}
