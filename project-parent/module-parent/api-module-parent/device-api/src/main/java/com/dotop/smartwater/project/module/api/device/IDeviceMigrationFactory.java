package com.dotop.smartwater.project.module.api.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceMigrationForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceMigrationHistoryForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationHistoryVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationVo;

/**
 * 设备迁移

 * @date 2019-08-09
 *
 */
public interface IDeviceMigrationFactory extends BaseFactory<DeviceMigrationForm, DeviceMigrationVo> {
	
	List<DeviceMigrationVo> tempPage(DeviceMigrationForm deviceMigrationForm);
	
	List<DeviceMigrationVo> pageMigration(DeviceMigrationForm deviceMigrationForm);
	
	Pagination<DeviceMigrationVo> detail(DeviceMigrationForm deviceMigrationForm);

	Integer clearTemp(DeviceMigrationForm deviceMigrationForm);

	Pagination<DeviceMigrationHistoryVo> pageHistory(DeviceMigrationHistoryForm deviceMigrationHistoryForm);
	
	String migrationDevice(DeviceMigrationHistoryForm deviceMigrationForm);

	String deleteHistory(DeviceMigrationHistoryForm deviceMigrationHistoryForm);
	
	Integer updateTemp(DeviceMigrationForm deviceMigrationForm);
}
