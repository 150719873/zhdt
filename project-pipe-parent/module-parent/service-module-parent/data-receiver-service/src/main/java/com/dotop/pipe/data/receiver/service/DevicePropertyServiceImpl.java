package com.dotop.pipe.data.receiver.service;

import java.util.Date;
import java.util.Map;

import com.dotop.pipe.data.receiver.api.cache.IDevicePropertyCache;
import com.dotop.pipe.data.receiver.api.dao.IDevicePropertyDao;
import com.dotop.pipe.data.receiver.api.service.IDevicePropertyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;

@Service
public class DevicePropertyServiceImpl implements IDevicePropertyService {

	private static final Logger LOGGER = LogManager.getLogger(DevicePropertyServiceImpl.class);

	@Autowired
	private IDevicePropertyDao iDevicePropertyDao;

	@Autowired
	private IDevicePropertyCache iDevicePropertyCache;

	@Override
	public DevicePropertyVo getCache(String deviceId, String field) {
		DevicePropertyVo deviceProperty = iDevicePropertyCache.hashGet(deviceId, field);
		return deviceProperty;
	}

	@Override
	public DevicePropertyVo getDb(String deviceId, String field) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			DevicePropertyVo deviceProperty = iDevicePropertyDao.get(deviceId, field, isDel);
			return deviceProperty;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	// 不需要事务
	public void add(String deviceId, String deviceCode, String field, String tag, String name, String unit, String val,
			Date devSendDate, Date serReceDate, Date curr, String userBy, String enterpriseId, String devProId) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			iDevicePropertyDao.add(devProId, deviceId, deviceCode, field, tag, name, unit, val, devSendDate,
					serReceDate, curr, userBy, isDel, enterpriseId);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	// 不需要事务
	public void upd(String devProId, String tag, String name, String unit, String val, Date devSendDate,
			Date serReceDate, Date curr, String userBy, String enterpriseId) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			iDevicePropertyDao.upd(devProId, tag, name, unit, val, devSendDate, serReceDate, curr, userBy, isDel,
					enterpriseId);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void addCache(DevicePropertyVo deviceProperty) {
		iDevicePropertyCache.set(deviceProperty);
	}

	@Override
	public boolean getCacheByDeviceCode(String deviceCode) {
		return iDevicePropertyCache.getByDeviceCode(deviceCode);
	}

	@Override
	public void addCacheByDeviceCode(String deviceCode) {
		iDevicePropertyCache.setByDeviceCode(deviceCode);
	}

	@Override
	public void addProperty(Map<String, Object> dataMap, String enterpriseId, String deviceId) {
		try {
			// 封装特殊字段参数
			Date date = new Date();
			dataMap.put("enterprise_id", enterpriseId);
			dataMap.put("device_id", deviceId);
			dataMap.put("dev_pro_id", UuidUtils.getUuid());
			dataMap.put("is_del", RootModel.NOT_DEL);
			dataMap.put("create_date", date);
			dataMap.put("ser_rece_date", date);
			dataMap.put("create_by", GlobalContext.SYSTEM_NAME);
			dataMap.put("last_by", GlobalContext.SYSTEM_NAME);
			dataMap.put("last_date", date);
			iDevicePropertyDao.addProperty(dataMap);
			dataMap.remove("dev_pro_id");
			dataMap.remove("last_by");
			dataMap.remove("last_date");
			dataMap.remove("create_by");
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void uptProperty(Map<String, Object> dataMap, String enterpriseId, String deviceId) {
		try {
			// 封装特殊字段参数
			Date date = new Date();
			dataMap.put("ser_rece_date", date);
			dataMap.put("last_by", GlobalContext.SYSTEM_NAME);
			dataMap.put("last_date", date);
			iDevicePropertyDao.uptProperty(dataMap, enterpriseId, deviceId);
			dataMap.remove("last_by");
			dataMap.remove("last_date");
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public boolean isExist(String deviceId, String enterpriseId) {
		try {
			return iDevicePropertyDao.isExist(enterpriseId, deviceId);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
