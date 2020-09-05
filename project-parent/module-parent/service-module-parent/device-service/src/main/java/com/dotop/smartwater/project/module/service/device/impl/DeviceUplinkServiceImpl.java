package com.dotop.smartwater.project.module.service.device.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.service.device.IDeviceUplinkService;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.QueryParamBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.module.core.water.dto.customize.QueryParamDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceUplinkDao;
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
public class DeviceUplinkServiceImpl implements IDeviceUplinkService {

	private static final Logger LOGGER = LogManager.getLogger(DeviceUplinkServiceImpl.class);

	@Autowired
	private IDeviceUplinkDao iDeviceUplinkDao;

	@Override
	public DeviceUplinkVo add(DeviceUplinkBo deviceUplinkBo) {
		try {
			// 参数转换
			DeviceUplinkDto deviceUplinkDto = new DeviceUplinkDto();
			BeanUtils.copyProperties(deviceUplinkBo, deviceUplinkDto);
			deviceUplinkDto.setId(UuidUtils.getUuid());
			iDeviceUplinkDao.add(deviceUplinkDto);
			DeviceUplinkVo deviceUplinkVo = new DeviceUplinkVo();
			BeanUtils.copyProperties(deviceUplinkDto, deviceUplinkVo);
			return deviceUplinkVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DeviceUplinkVo findLastUplinkWater(String devid, String date) {
		try {
			return iDeviceUplinkDao.findLastUplinkWater(devid, date);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<OriginalVo> findOriginal(QueryParamBo queryParamBo) {
		try {
			// 参数转换
			QueryParamDto queryParamDto = new QueryParamDto();
			BeanUtils.copyProperties(queryParamBo, queryParamDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(queryParamBo.getPage(), queryParamBo.getPageCount());
			List<OriginalVo> list = iDeviceUplinkDao.findOriginal(queryParamDto);
			// 拼接数据返回
			return new Pagination<>(queryParamBo.getPage(), queryParamBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<OriginalVo> findOriginalCrossMonth(QueryParamBo queryParamBo) {
		try {
			// 参数转换
			QueryParamDto queryParamDto = new QueryParamDto();
			BeanUtils.copyProperties(queryParamBo, queryParamDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(queryParamBo.getPage(), queryParamBo.getPageCount());
			List<OriginalVo> list = iDeviceUplinkDao.findOriginalCrossMonth(queryParamDto);
			// 拼接数据返回
			return new Pagination<>(queryParamBo.getPage(), queryParamBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public Pagination<OriginalVo> exportFindOriginal(QueryParamBo queryParamBo) {
		try {
			// 参数转换
			QueryParamDto queryParamDto = new QueryParamDto();
			BeanUtils.copyProperties(queryParamBo, queryParamDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(queryParamBo.getPage(), queryParamBo.getPageCount());
			List<OriginalVo> list = iDeviceUplinkDao.exportFindOriginal(queryParamDto);
			// 拼接数据返回
			return new Pagination<>(queryParamBo.getPage(), queryParamBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<OriginalVo> exportFindOriginalCrossMonth(QueryParamBo queryParamBo) {
		try {
			// 参数转换
			QueryParamDto queryParamDto = new QueryParamDto();
			BeanUtils.copyProperties(queryParamBo, queryParamDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(queryParamBo.getPage(), queryParamBo.getPageCount());
			List<OriginalVo> list = iDeviceUplinkDao.exportFindOriginalCrossMonth(queryParamDto);
			// 拼接数据返回
			return new Pagination<>(queryParamBo.getPage(), queryParamBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	public List<OriginalVo> findUplinkData(QueryParamBo queryParamBo) {
		try {
			// 参数转换
			QueryParamDto queryParamDto = new QueryParamDto();
			BeanUtils.copyProperties(queryParamBo, queryParamDto);
			return iDeviceUplinkDao.findUplinkData(queryParamDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	public List<OriginalVo> findCrossMonthUplinkData(QueryParamBo queryParamBo) {
		try {
			// 参数转换
			QueryParamDto queryParamDto = new QueryParamDto();
			BeanUtils.copyProperties(queryParamBo, queryParamDto);
			return iDeviceUplinkDao.findCrossMonthUplinkData(queryParamDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	

	@Override
	public Pagination<DeviceUplinkVo> findDownLink(DeviceBo deviceBo, String start, String end) {
		try {
			// 参数转换
			DeviceUplinkDto deviceUplinkDto = new DeviceUplinkDto();
			BeanUtils.copyProperties(deviceBo, deviceUplinkDto);
			Page<Object> pageHelper = PageHelper.startPage(deviceBo.getPage(), deviceBo.getPageCount());
			List<DeviceUplinkVo> list = iDeviceUplinkDao.findDownLink(deviceUplinkDto);
			return new Pagination<>(deviceBo.getPage(), deviceBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceVo> getUplinkData(String deveui, String month) {
		try {
			return iDeviceUplinkDao.getUplinkData(deveui, month);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public OriginalVo getOriginalByIdAndDate(String id, String month) {
		try {
			return iDeviceUplinkDao.getOriginalByIdAndDate(id, month);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceUplinkVo> batchFindWaterByDeveuis(String deveuis, String systime, String rxtime) {
		// TODO Auto-generated method stub
		try {
			return iDeviceUplinkDao.batchFindWaterByDeveuis(deveuis, systime, rxtime);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
