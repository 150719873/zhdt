package com.dotop.smartwater.project.module.service.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessDbFieldBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessDbFieldVo;

public interface IProcessDbFieldService extends BaseService<WorkCenterProcessDbFieldBo, WorkCenterProcessDbFieldVo> {

	@Override
	WorkCenterProcessDbFieldVo add(WorkCenterProcessDbFieldBo workCenterProcessDbFieldBo);

}
