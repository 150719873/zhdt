package com.dotop.pipe.service.device;

import com.dotop.pipe.api.dao.device.IDeviceMappingDao;
import com.dotop.pipe.api.service.device.IDeviceMappingService;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.bo.device.DeviceMappingBo;
import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.dto.decive.DeviceMappingDto;
import com.dotop.pipe.core.vo.device.DeviceMappingVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceMappingServiceImpl implements IDeviceMappingService {

	private static final Logger logger = LogManager.getLogger(DeviceMappingServiceImpl.class);

	@Autowired
	private IDeviceMappingDao iDeviceMapDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceVo add(DeviceBo deviceBo) {
		try {
			String mapId = UuidUtils.getUuid();
			Integer isDel = RootModel.NOT_DEL;
			DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
			deviceDto.setDeviceMapId(mapId);
			deviceDto.setIsDel(isDel);
			iDeviceMapDao.add(deviceDto);
			return null;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public String del(DeviceBo deviceBo) {
		try {
			Integer isDel = RootModel.DEL;
			DeviceDto deviceDto = BeanUtils.copyProperties(deviceBo, DeviceDto.class);
			deviceDto.setIsDel(isDel);
			iDeviceMapDao.del(deviceDto);
			return null;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceMappingVo addMapping(DeviceMappingBo deviceMappingBo) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			DeviceMappingDto  deviceMappingDto = BeanUtils.copyProperties(deviceMappingBo, DeviceMappingDto.class);
			deviceMappingDto.setIsDel(isDel);
			iDeviceMapDao.addMapping(deviceMappingDto);
			return null;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceMappingVo delMapping(DeviceMappingBo deviceMappingBo) {
		try {
			Integer isDel = RootModel.DEL;
			DeviceMappingDto  deviceMappingDto = BeanUtils.copyProperties(deviceMappingBo, DeviceMappingDto.class);
			deviceMappingDto.setIsDel(isDel);
			iDeviceMapDao.delMapping(deviceMappingDto);
			return null;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceMappingVo> mappingList(DeviceMappingBo deviceMappingBo) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			DeviceMappingDto  deviceMappingDto = BeanUtils.copyProperties(deviceMappingBo, DeviceMappingDto.class);
			deviceMappingDto.setIsDel(isDel);
			List<DeviceMappingVo> list = iDeviceMapDao.mappingList(deviceMappingDto);
			return list;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}


	@Override
	public List<DeviceMappingVo> regionMappingList(DeviceMappingBo deviceMappingBo) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			DeviceMappingDto  deviceMappingDto = BeanUtils.copyProperties(deviceMappingBo, DeviceMappingDto.class);
			deviceMappingDto.setIsDel(isDel);
			List<DeviceMappingVo> list = iDeviceMapDao.regionMappingList(deviceMappingDto);
			return list;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}
}
