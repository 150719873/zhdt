package com.dotop.smartwater.project.module.service.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceMigrationBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceMigrationHistoryBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationHistoryVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationVo;

/**
 * 设备迁移

 * @date 2019-08-09
 *
 */
public interface IDeviceMigrationService extends BaseService<DeviceMigrationBo, DeviceMigrationVo> {

	List<DeviceMigrationVo> tempPage(DeviceMigrationBo deviceMigrationBo);

	List<DeviceMigrationVo> pageMigration(DeviceMigrationBo deviceMigrationBo);
	
	Pagination<DeviceMigrationVo> detail(DeviceMigrationBo deviceMigrationBo);

	Integer clearTemp(DeviceMigrationBo deviceMigrationBo);

	Pagination<DeviceMigrationHistoryVo> pageHistory(DeviceMigrationHistoryBo deviceMigrationHistoryBo);

	String migrationDevice(DeviceMigrationHistoryBo deviceMigrationHistoryBo);

	String deleteHistory(DeviceMigrationHistoryBo deviceMigrationHistoryBo);
	
	Integer updateTemp(DeviceMigrationBo deviceMigrationBo);
}
