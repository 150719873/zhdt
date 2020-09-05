package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallAcceptanceDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallAcceptanceVo;

import java.util.List;

/**
 * 工程验收
 *

 * @date 2019年3月12日
 */
public interface IInstallAppointmentAcceptanceDao extends BaseDao<InstallAcceptanceDto, InstallAcceptanceVo> {

	@Override
	InstallAcceptanceVo get(InstallAcceptanceDto dto);

	int updateAccept(InstallAcceptanceDto dto);

	int submitAccept(InstallAcceptanceDto dto);

	int accept(InstallAcceptanceDto dto);

	List<InstallAcceptanceVo> getList(InstallAcceptanceDto dto);
}
