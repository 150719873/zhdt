package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallAmountDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallAmountVo;

/**
 * 费用管理
 *

 * @date 2019年3月12日
 */
public interface IInstallAppointmentAmountDao extends BaseDao<InstallAmountDto, InstallAmountVo> {

	@Override
	InstallAmountVo get(InstallAmountDto dto);

	int updateAmount(InstallAmountDto dto);

	int submitAmount(InstallAmountDto dto);

}
