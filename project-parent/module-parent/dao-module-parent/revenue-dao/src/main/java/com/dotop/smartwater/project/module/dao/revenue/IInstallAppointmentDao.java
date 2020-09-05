package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallAppointmentDetailDto;
import com.dotop.smartwater.project.module.core.water.dto.InstallAppointmentDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentVo;

import java.util.List;

public interface IInstallAppointmentDao extends BaseDao<InstallAppointmentDto, InstallAppointmentVo> {

	List<InstallAppointmentVo> getList(InstallAppointmentDto dto);

	int generateAppointment(InstallAppointmentDto dto);

	List<InstallAppointmentDetailVo> getAppointmentDetail(InstallAppointmentDetailDto dto);

	int setTemp(InstallAppointmentDto dto);

	int delete(InstallAppointmentDto dto);

	@Override
	InstallAppointmentVo get(InstallAppointmentDto dto);

	int updateAppointmentStatus(InstallAppointmentDto dto);

	int updateAppointment(InstallAppointmentDto dto);

	int appointmentNumber(InstallAppointmentDto dto);

	int checkNohandles(InstallAppointmentDto dto);

}
