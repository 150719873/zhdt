package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallContractDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallContractVo;

/**
 * 签订合同
 *

 * @date 2019年3月12日
 */
public interface IInstallAppointmentContractDao extends BaseDao<InstallContractDto, InstallContractVo> {

	@Override
	InstallContractVo get(InstallContractDto dto);

	int updateContract(InstallContractDto dto);

	int submitContract(InstallContractDto dto);

}
