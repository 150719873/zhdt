package com.dotop.pipe.data.receiver.factory;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.data.receiver.api.factory.IDeviceDataFactory;
import com.dotop.pipe.data.receiver.api.service.IDevicePropertyLogService;
import com.dotop.pipe.data.receiver.api.service.IDevicePropertyService;

@Component
public class DeviceDataFactoryImpl implements IDeviceDataFactory {

	private static final Logger LOGGER = LogManager.getLogger(DeviceDataFactoryImpl.class);

	@Autowired
	private IDevicePropertyService iDevicePropertyService;

	@Autowired
	private IDevicePropertyLogService iDevicePropertyLogService;

	/**
	 * 添加属性日志表
	 * 
	 * @param enterpriseId
	 * @param deviceId
	 */
	public void addPropertyLog(JSONObject originalData, String deviceId, String enterpriseId,
			Map<String, Object> dataMap) {
		iDevicePropertyLogService.addProperty(dataMap, enterpriseId, deviceId, originalData);
	}

	@Override
	public void addProperty(String deviceId, String enterpriseId, Map<String, Object> dataMap) {
		String deviceCode = dataMap.get("device_code").toString();
		boolean flag = iDevicePropertyService.isExist(deviceId, enterpriseId);
		if (flag) {
			// 更新
			iDevicePropertyService.uptProperty(dataMap, enterpriseId, deviceId);
		} else {
			// 新增
			iDevicePropertyService.addProperty(dataMap, enterpriseId, deviceId);
		}
	}

	@Override
	public boolean getCacheByDeviceCode(String deviceCode) {
		return iDevicePropertyService.getCacheByDeviceCode(deviceCode);
	}

	@Override
	public void setCacheByDeviceCode(String deviceCode) {
		iDevicePropertyService.addCacheByDeviceCode(deviceCode);
	}
}
