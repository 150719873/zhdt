package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallApplyDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallApplyVo;

/**
 * 报装申请
 *

 * @date 2019年3月12日
 */
public interface IInstallAppointmentApplyDao extends BaseDao<InstallApplyDto, InstallApplyVo> {

	@Override
	InstallApplyVo get(InstallApplyDto dto);

	int submitApply(InstallApplyDto dto);

	int generateApply(InstallApplyDto dto);

}
