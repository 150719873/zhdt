package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallShipmentDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallShipmentVo;

/**
 * 仓库出货
 *

 * @date 2019年3月12日
 */
public interface IInstallAppointmentShipmentDao extends BaseDao<InstallShipmentDto, InstallShipmentVo> {

	@Override
	InstallShipmentVo get(InstallShipmentDto dto);

	int updateShip(InstallShipmentDto dto);

	int submitShip(InstallShipmentDto dto);
}
