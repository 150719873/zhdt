package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallAppointmentRelationDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentRelationVo;

import java.util.List;

public interface IInstallAppointmentRelationDao
		extends BaseDao<InstallAppointmentRelationDto, InstallAppointmentRelationVo> {

	int generateRelations(InstallAppointmentRelationDto rdto);

	List<InstallAppointmentRelationVo> getRelations(InstallAppointmentRelationDto rdto);

}
