package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallSurveyDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallSurveyVo;

import java.util.List;

/**
 * 现场勘测
 *

 * @date 2019年3月12日
 */
public interface IInstallAppointmentSurveyDao extends BaseDao<InstallSurveyDto, InstallSurveyVo> {

	@Override
	InstallSurveyVo get(InstallSurveyDto dto);

	int submitSurvey(InstallSurveyDto dto);

	int updateSurvey(InstallSurveyDto dto);

	int survey(InstallSurveyDto dto);

	List<InstallSurveyVo> getList(InstallSurveyDto dto);

}
