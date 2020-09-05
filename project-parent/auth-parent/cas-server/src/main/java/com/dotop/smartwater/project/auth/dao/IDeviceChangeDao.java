package com.dotop.smartwater.project.auth.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceChangeDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;

public interface IDeviceChangeDao extends BaseDao<DeviceChangeDto, DeviceChangeVo> {
	
	List<DeviceChangeVo> getList(DeviceChangeDto dto) throws DataAccessException;
	
}
