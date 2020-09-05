package com.dotop.smartwater.project.module.service.device.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceWarningSettingBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceWarningSettingDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningSettingVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceWarningSettingDao;
import com.dotop.smartwater.project.module.service.device.IDeviceWarningSettingService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**

 * @date 2019/4/1.
 */
@Service
public class DeviceWarningSettingServiceImpl implements IDeviceWarningSettingService {

	private static final Logger LOGGER = LogManager.getLogger(DeviceWarningSettingServiceImpl.class);

	@Autowired
	private IDeviceWarningSettingDao iDeviceWarningSettingDao;

	@Override
	public Pagination<DeviceWarningSettingVo> page(DeviceWarningSettingBo deviceWarningSettingBo) {
		try {
			// 参数转换
			DeviceWarningSettingDto deviceWarningSettingDto = new DeviceWarningSettingDto();
			BeanUtils.copyProperties(deviceWarningSettingBo, deviceWarningSettingDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(deviceWarningSettingBo.getPage(),
					deviceWarningSettingBo.getPageCount());
			List<DeviceWarningSettingVo> list = iDeviceWarningSettingDao.list(deviceWarningSettingDto);
			return new Pagination<>(deviceWarningSettingBo.getPage(), deviceWarningSettingBo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceWarningSettingVo> list(DeviceWarningSettingBo deviceWarningSettingBo) {
		try {
			// 参数转换
			DeviceWarningSettingDto deviceWarningSettingDto = new DeviceWarningSettingDto();
			BeanUtils.copyProperties(deviceWarningSettingBo, deviceWarningSettingDto);
			// 操作数据
			return iDeviceWarningSettingDao.list(deviceWarningSettingDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DeviceWarningSettingVo edit(DeviceWarningSettingBo deviceWarningSettingBo) {
		try {
			// 参数转换
			DeviceWarningSettingVo deviceWarningSettingVo = new DeviceWarningSettingVo();
			DeviceWarningSettingDto deviceWarningSettingDto = new DeviceWarningSettingDto();
			BeanUtils.copyProperties(deviceWarningSettingBo, deviceWarningSettingDto);
			BeanUtils.copyProperties(deviceWarningSettingBo, deviceWarningSettingVo);
			// 操作数据
			iDeviceWarningSettingDao.edit(deviceWarningSettingDto);
			return deviceWarningSettingVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
