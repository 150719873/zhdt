package com.dotop.pipe.data.receiver.api.service;

import java.util.Date;
import java.util.Map;

import com.dotop.pipe.core.vo.device.DevicePropertyVo;

public interface IDevicePropertyService {

	DevicePropertyVo getCache(String deviceId, String field);

	DevicePropertyVo getDb(String deviceId, String field);

	void add(String deviceId, String deviceCode, String field, String tag, String name, String unit, String val,
			Date devSendDate, Date serReceDate, Date curr, String userBy, String enterpriseId, String devProId);

	void upd(String devProId, String tag, String name, String unit, String val, Date devSendDate, Date serReceDate,
			Date curr, String userBy, String enterpriseId);

	void addCache(DevicePropertyVo deviceProperty);

	boolean getCacheByDeviceCode(String deviceCode);

	void addCacheByDeviceCode(String deviceCode);

	/**
	 * 动态插入数据接口
	 * 
	 * @param propertyMap
	 */
	void addProperty(Map<String, Object> dataMap, String enterpriseId, String deviceId);

	void uptProperty(Map<String, Object> dataMap, String enterpriseId, String deviceId);

	boolean isExist(String deviceId, String enterpriseId);

}
