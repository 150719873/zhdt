package com.dotop.smartwater.project.module.dao.device;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceMigrationDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceMigrationHistoryDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationHistoryVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationVo;

/**
 * 设备迁移

 * @date 2010-08-09
 */

public interface IDeviceMigrationDao extends BaseDao<DeviceMigrationDto, DeviceMigrationVo> {
	
	List<DeviceMigrationVo> tempPage(DeviceMigrationDto deviceMigrationDto);
	
	List<DeviceMigrationVo> tempList(DeviceMigrationDto deviceMigrationDto);

	List<DeviceMigrationVo> page(DeviceMigrationDto deviceMigrationDto);

	List<DeviceMigrationVo> detail(DeviceMigrationDto deviceMigrationDto);
	
	Integer clearTemp();

	List<DeviceMigrationHistoryVo> pageHistory(DeviceMigrationHistoryDto deviceMigrationHistoryDto);
	
	Integer migrateDevice(@Param("list") List<DeviceMigrationDto> list, @Param("history") DeviceMigrationHistoryDto deviceMigrationHistoryDto);
	
	Integer insertMigration(DeviceMigrationDto deviceMigrationDto);

	Integer batchInsertMigration(@Param("list") List<DeviceMigrationDto> list);
	
	Integer insertTemp(DeviceMigrationDto deviceMigrationDto);
	
	Integer batchInsertTemp(@Param("list") List<DeviceMigrationDto> list);

	Integer insertTempSelect(DeviceMigrationDto deviceMigrationDto);
	
	Integer insertHistory(DeviceMigrationHistoryDto deviceMigrationHistoryDto);
	
	Integer updateTemp(DeviceMigrationDto deviceMigrationDto);
	
	Integer judgeDevice(DeviceMigrationDto deviceMigrationDto);
	
	Integer deleteHistory(DeviceMigrationHistoryDto deviceMigrationHistoryDto);
	
	Integer deleteMigration(DeviceMigrationHistoryDto deviceMigrationHistoryDto);
}
