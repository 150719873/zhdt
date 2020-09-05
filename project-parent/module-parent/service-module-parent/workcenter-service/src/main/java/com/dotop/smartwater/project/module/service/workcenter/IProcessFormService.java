package com.dotop.smartwater.project.module.service.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessFormBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessFormVo;

public interface IProcessFormService extends BaseService<WorkCenterProcessFormBo, WorkCenterProcessFormVo> {

	@Override
	WorkCenterProcessFormVo add(WorkCenterProcessFormBo workCenterProcessFormBo);

}
