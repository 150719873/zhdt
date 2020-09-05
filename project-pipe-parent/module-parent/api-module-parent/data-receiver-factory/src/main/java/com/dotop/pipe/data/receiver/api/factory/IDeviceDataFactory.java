package com.dotop.pipe.data.receiver.api.factory;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface IDeviceDataFactory {

	/**
	 * 添加设备属性日志表
	 * 
	 * @param data
	 * @param originalData
	 * @param deviceId
	 * @param enterpriseId
	 * @param devicePropertyLogMap
	 */
	void addPropertyLog(JSONObject originalData, String deviceId, String enterpriseId,
			Map<String, Object> devicePropertyLogMap);

	boolean getCacheByDeviceCode(String deviceCode);

	void setCacheByDeviceCode(String deviceCode);

	/**
	 * 添加设备属性
	 * 
	 * @param data
	 * @param originalData
	 * @param deviceId
	 * @param enterpriseId
	 * @param devicePropertyLogMap
	 */
	void addProperty(String deviceId, String enterpriseId, Map<String, Object> devicePropertyLogMap);

}
