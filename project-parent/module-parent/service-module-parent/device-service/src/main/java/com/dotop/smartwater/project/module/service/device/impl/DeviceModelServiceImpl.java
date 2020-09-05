package com.dotop.smartwater.project.module.service.device.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.device.IDeviceModelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceModelBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceModelDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceModelVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceModelDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**

 * @date 2019/2/26.
 */
@Service
public class DeviceModelServiceImpl implements IDeviceModelService {

	private static final Logger LOGGER = LogManager.getLogger(DeviceModelServiceImpl.class);

	@Autowired
	private IDeviceModelDao iDeviceModelDao;

	@Override
	public Pagination<DeviceModelVo> find(DeviceModelBo deviceModelBo) {
		try {
			// 参数转换
			DeviceModelDto deviceModelDto = new DeviceModelDto();
			BeanUtils.copyProperties(deviceModelBo, deviceModelDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(deviceModelBo.getPage(), deviceModelBo.getPageCount());
			List<DeviceModelVo> list = iDeviceModelDao.getList(deviceModelDto);
			// 拼接数据返回
			return new Pagination<>(deviceModelBo.getPage(), deviceModelBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceModelVo> noPagingfind(DeviceModelBo deviceModelBo) {
		try {
			// 参数转换
			DeviceModelDto deviceModelDto = new DeviceModelDto();
			BeanUtils.copyProperties(deviceModelBo, deviceModelDto);
			// 操作数据
			return iDeviceModelDao.getList(deviceModelDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void save(DeviceModelBo deviceModelBo) {
		try {
			// 参数转换
			DeviceModelDto deviceModelDto = new DeviceModelDto();
			BeanUtils.copyProperties(deviceModelBo, deviceModelDto);
			// 操作数据
			iDeviceModelDao.save(deviceModelDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void update(DeviceModelBo deviceModelBo) {
		try {
			// 参数转换
			DeviceModelDto deviceModelDto = new DeviceModelDto();
			BeanUtils.copyProperties(deviceModelBo, deviceModelDto);
			// 操作数据
			iDeviceModelDao.update(deviceModelDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void delete(DeviceModelBo deviceModelBo) {
		try {
			// 参数转换
			DeviceModelDto deviceModelDto = new DeviceModelDto();
			BeanUtils.copyProperties(deviceModelBo, deviceModelDto);
			iDeviceModelDao.delete(deviceModelDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DeviceModelVo get(DeviceModelBo deviceModelBo) {
		try {
			// 参数转换
			DeviceModelDto deviceModelDto = new DeviceModelDto();
			BeanUtils.copyProperties(deviceModelBo, deviceModelDto);
			return iDeviceModelDao.get(deviceModelDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
