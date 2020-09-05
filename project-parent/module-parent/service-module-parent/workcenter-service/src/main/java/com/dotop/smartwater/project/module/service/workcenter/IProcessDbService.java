package com.dotop.smartwater.project.module.service.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessDbBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessDbVo;

public interface IProcessDbService extends BaseService<WorkCenterProcessDbBo, WorkCenterProcessDbVo> {

	@Override
	WorkCenterProcessDbVo add(WorkCenterProcessDbBo workCenterProcessDbBo);
}
