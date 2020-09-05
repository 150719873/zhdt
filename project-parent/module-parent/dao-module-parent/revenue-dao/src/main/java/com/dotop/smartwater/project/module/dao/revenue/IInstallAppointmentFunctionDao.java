package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallAppointmentFunctionDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentFunctionVo;

public interface IInstallAppointmentFunctionDao
		extends BaseDao<InstallAppointmentFunctionDto, InstallAppointmentFunctionVo> {

	int generateFuncs(InstallAppointmentFunctionDto fdto);

}
