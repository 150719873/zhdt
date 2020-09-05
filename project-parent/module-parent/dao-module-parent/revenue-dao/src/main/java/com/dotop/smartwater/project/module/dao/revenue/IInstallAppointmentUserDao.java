package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallUserDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallUserVo;

import java.util.List;

/**
 * 导入用户档案
 *

 * @date 2019年3月12日
 */
public interface IInstallAppointmentUserDao extends BaseDao<InstallUserDto, InstallUserVo> {

	List<InstallUserVo> getUsers(InstallUserDto dto);

	int addUser(InstallUserDto dto);

	int editUser(InstallUserDto dto);

	int delUser(InstallUserDto dto);

	int updateStatus(InstallUserDto dto);

	List<InstallUserVo> getList(InstallUserDto dto);

}
