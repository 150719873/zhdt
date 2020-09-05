package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallChangeDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallChangeVo;

/**
 * 换表申请
 *

 * @date 2019年3月12日
 */
public interface IInstallAppointmentChangeDao extends BaseDao<InstallChangeDto, InstallChangeVo> {

	@Override
	InstallChangeVo get(InstallChangeDto dto);

	int generateChange(InstallChangeDto dto);

}
