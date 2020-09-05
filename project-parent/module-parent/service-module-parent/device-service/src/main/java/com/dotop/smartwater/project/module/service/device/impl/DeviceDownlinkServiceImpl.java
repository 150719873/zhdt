package com.dotop.smartwater.project.module.service.device.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.service.device.IDeviceDownlinkService;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.QueryParamBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceDownlinkDto;
import com.dotop.smartwater.project.module.core.water.dto.customize.QueryParamDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceDownlinkDao;
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

 * @date 2019/2/25.
 */
@Service
public class DeviceDownlinkServiceImpl implements IDeviceDownlinkService {

	private static final Logger LOGGER = LogManager.getLogger(DeviceDownlinkServiceImpl.class);

	@Autowired
	private IDeviceDownlinkDao iDeviceDownlinkDao;

	@Override
	public DeviceDownlinkVo add(DeviceDownlinkBo deviceDownlinkBo) {
		try {
			// 参数转换
			DeviceDownlinkDto deviceDownlinkDto = new DeviceDownlinkDto();
			BeanUtils.copyProperties(deviceDownlinkBo, deviceDownlinkDto);
			deviceDownlinkDto.setId(UuidUtils.getUuid());
			iDeviceDownlinkDao.add(deviceDownlinkDto);
			DeviceDownlinkVo deviceDownlinkVo = new DeviceDownlinkVo();
			BeanUtils.copyProperties(deviceDownlinkVo, deviceDownlinkDto);
			return deviceDownlinkVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceDownlinkVo> findByClientId(DeviceDownlinkBo deviceDownlinkBo) {
		try {
			// 参数转换
			DeviceDownlinkDto deviceDownlinkDto = new DeviceDownlinkDto();
			BeanUtils.copyProperties(deviceDownlinkBo, deviceDownlinkDto);
			return iDeviceDownlinkDao.findByClientId(deviceDownlinkDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void update(DeviceDownlinkBo deviceDownlinkBo) {
		try {
			// 参数转换
			DeviceDownlinkDto deviceDownlinkDto = new DeviceDownlinkDto();
			BeanUtils.copyProperties(deviceDownlinkBo, deviceDownlinkDto);
			iDeviceDownlinkDao.update(deviceDownlinkDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DeviceDownlinkVo get(DeviceDownlinkBo deviceDownlinkBo) {
		try {
			// 参数转换
			DeviceDownlinkDto deviceDownlinkDto = new DeviceDownlinkDto();
			BeanUtils.copyProperties(deviceDownlinkBo, deviceDownlinkDto);
			return iDeviceDownlinkDao.get(deviceDownlinkDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<DeviceDownlinkVo> page(DeviceDownlinkBo deviceDownlinkBo) {
		try {
			// 参数转换
			DeviceDownlinkDto deviceDownlinkDto = new DeviceDownlinkDto();
			BeanUtils.copyProperties(deviceDownlinkBo, deviceDownlinkDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(deviceDownlinkBo.getPage(), deviceDownlinkBo.getPageCount());
			List<DeviceDownlinkVo> list = iDeviceDownlinkDao.list(deviceDownlinkDto);
			return new Pagination<>(deviceDownlinkBo.getPage(), deviceDownlinkBo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<DeviceDownlinkVo> getHistory(QueryParamBo queryParamBo) {

		try {
			QueryParamDto queryParamDto = new QueryParamDto();
			BeanUtils.copyProperties(queryParamBo, queryParamDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(queryParamBo.getPage(), queryParamBo.getPageCount());
			List<DeviceDownlinkVo> list = iDeviceDownlinkDao.getHistory(queryParamDto);
			return new Pagination<>(queryParamBo.getPage(), queryParamBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceDownlinkVo> list(DeviceDownlinkBo deviceDownlinkBo) {
		try {
			// 参数转换
			DeviceDownlinkDto deviceDownlinkDto = new DeviceDownlinkDto();
			BeanUtils.copyProperties(deviceDownlinkBo, deviceDownlinkDto);

			return iDeviceDownlinkDao.list(deviceDownlinkDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}


	@Override
	public DeviceDownlinkVo getLastDownLink(String devid) {
		try {
			return iDeviceDownlinkDao.getLastDownLink(devid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
