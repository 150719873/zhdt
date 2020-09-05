package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceParametersBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceBatchDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceParametersDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceParametersVo;
import com.dotop.smartwater.project.module.dao.tool.IDeviceParametersDao;
import com.dotop.smartwater.project.module.service.tool.IDeviceParametersService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 

 * @date 2019年2月21日
 */
@Service
public class DeviceParametersServiceImpl implements IDeviceParametersService {

	private static final Logger LOGGER = LogManager.getLogger(DeviceParametersServiceImpl.class);

	@Autowired
	private IDeviceParametersDao iDeviceParametersDao;

	@Override
	public Pagination<DeviceParametersVo> page(DeviceParametersBo deviceParametersBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DeviceParametersDto deviceParametersDto = new DeviceParametersDto();
			BeanUtils.copyProperties(deviceParametersBo, deviceParametersDto);
			deviceParametersDto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(deviceParametersBo.getPage(),
					deviceParametersBo.getPageCount());
			List<DeviceParametersVo> list = iDeviceParametersDao.list(deviceParametersDto);
			Pagination<DeviceParametersVo> pagination = new Pagination<>(deviceParametersBo.getPageCount(),
					deviceParametersBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DeviceParametersVo get(DeviceParametersBo deviceParametersBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DeviceParametersDto deviceParametersDto = new DeviceParametersDto();

			deviceParametersDto.setEnterpriseid(deviceParametersBo.getEnterpriseid());
			deviceParametersDto.setIsDel(isNotDel);
			deviceParametersDto.setDeviceParId(deviceParametersBo.getDeviceParId());
			return iDeviceParametersDao.get(deviceParametersDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	
	public DeviceParametersVo getParams(DeviceParametersBo deviceParametersBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DeviceParametersDto deviceParametersDto = new DeviceParametersDto();

			deviceParametersDto.setEnterpriseid(deviceParametersBo.getEnterpriseid());
			deviceParametersDto.setIsDel(isNotDel);
			deviceParametersDto.setDeviceParId(deviceParametersBo.getDeviceParId());
			return iDeviceParametersDao.getParams(deviceParametersDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceParametersVo add(DeviceParametersBo deviceParametersBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			String deviceParId = UuidUtils.getUuid();
			// 参数转换
			DeviceParametersDto deviceParametersDto = new DeviceParametersDto();
			BeanUtils.copyProperties(deviceParametersBo, deviceParametersDto);
			deviceParametersDto.setDeviceParId(deviceParId);
			deviceParametersDto.setIsDel(isNotDel);
			deviceParametersDto.setStatus("1");// 启用参数
			iDeviceParametersDao.add(deviceParametersDto);

			DeviceParametersVo deviceParametersVo = new DeviceParametersVo();
			deviceParametersVo.setDeviceParId(deviceParId);
			return deviceParametersVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceParametersVo> list(DeviceParametersBo deviceParametersBo) {
		Integer isNotDel = RootModel.NOT_DEL;
		// 参数转换
		DeviceParametersDto deviceParametersDto = new DeviceParametersDto();
		deviceParametersDto.setEnterpriseid(deviceParametersBo.getEnterpriseid());
		deviceParametersDto.setIsDel(isNotDel);
		return iDeviceParametersDao.list(deviceParametersDto);
	}
	
	
	public List<DeviceParametersVo> noEndList(DeviceBatchBo bo) {
		Integer isNotDel = RootModel.NOT_DEL;
		// 参数转换
		DeviceBatchDto dto = new DeviceBatchDto();
		dto.setEnterpriseid(bo.getEnterpriseid());
		dto.setIsDel(isNotDel);
		return iDeviceParametersDao.noEndList(dto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceParametersVo edit(DeviceParametersBo deviceParametersBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DeviceParametersDto deviceParametersDto = new DeviceParametersDto();
			BeanUtils.copyProperties(deviceParametersBo, deviceParametersDto);
			deviceParametersDto.setIsDel(isNotDel);
			iDeviceParametersDao.edit(deviceParametersDto);
			return new DeviceParametersVo();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(DeviceParametersBo deviceParametersBo) {
		try {
			Integer isDel = RootModel.DEL;
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DeviceParametersDto deviceParametersDto = new DeviceParametersDto();
			deviceParametersDto.setDeviceParId(deviceParametersBo.getDeviceParId());
			deviceParametersDto.setEnterpriseid(deviceParametersBo.getEnterpriseid());
			deviceParametersDto.setIsDel(isNotDel);
			deviceParametersDto.setNewIsDel(isDel);
			iDeviceParametersDao.edit(deviceParametersDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(DeviceParametersBo deviceParametersBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DeviceParametersDto deviceParametersDto = new DeviceParametersDto();
			BeanUtils.copyProperties(deviceParametersBo, deviceParametersDto);
			deviceParametersDto.setIsDel(isNotDel);
			return iDeviceParametersDao.isExist(deviceParametersDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	public boolean checkDeviceName(DeviceParametersBo deviceParametersBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DeviceParametersDto deviceParametersDto = new DeviceParametersDto();
			BeanUtils.copyProperties(deviceParametersBo, deviceParametersDto);
			deviceParametersDto.setIsDel(isNotDel);
			return iDeviceParametersDao.checkDeviceName(deviceParametersDto) > 0 ? false : true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
