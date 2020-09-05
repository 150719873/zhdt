package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.InstallAcceptanceBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallAmountBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallApplyBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallAppointmentBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallAppointmentDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallContractBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallShipmentBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallSurveyBo;
import com.dotop.smartwater.project.module.core.water.bo.InstallUserBo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAcceptanceVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallApplyVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallSurveyVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallUserVo;

public interface IInstallAppointmentService extends BaseService<InstallAppointmentBo, InstallAppointmentVo> {

	@Override
	Pagination<InstallAppointmentVo> page(InstallAppointmentBo bo);

	Pagination<InstallUserVo> pageUser(InstallUserBo bo);

	List<InstallAppointmentDetailVo> getAppointmentDetail(InstallAppointmentDetailBo bo);

	boolean setTemp(InstallAppointmentBo bo);

	int addUser(InstallUserBo bo);

	int editUser(InstallUserBo bo);

	int delUser(InstallUserBo bo);

	List<InstallUserVo> getUsers(InstallUserBo bo);

	int updateStatus(InstallUserBo bo);

	boolean delete(InstallAppointmentBo bo);

	@Override
	InstallAppointmentVo get(InstallAppointmentBo bo);

	InstallAppointmentVo detail(InstallAppointmentBo bo);

	InstallAppointmentVo getAppo(InstallAppointmentBo bo);

	InstallApplyVo getApply(InstallAppointmentBo bo);

	int submitApply(InstallApplyBo bo);

	int submitSurvey(InstallSurveyBo bo);

	Pagination<InstallSurveyVo> surveyPage(InstallSurveyBo bo);

	Pagination<InstallAcceptanceVo> acceptancePage(InstallAcceptanceBo bo);

	int submitContract(InstallContractBo bo);

	int submitAmount(InstallAmountBo bo);

	int submitShip(InstallShipmentBo bo);

	int submitAcceptance(InstallAcceptanceBo bo);

	int acceptance(InstallAcceptanceBo bo);

	InstallAcceptanceVo getAcceptance(InstallAcceptanceBo bo);

	int survey(InstallSurveyBo bo);

	boolean next(InstallAppointmentBo bo);
	
	boolean prev(InstallAppointmentBo bo);
	
	boolean end(InstallAppointmentBo bo);

	boolean inspectNode(InstallAppointmentBo bo);

	boolean save(InstallAppointmentBo bo);

	int checkAppointmentNumber(InstallAppointmentBo bo);

	int checkNohandles(InstallAppointmentBo bo);

}
