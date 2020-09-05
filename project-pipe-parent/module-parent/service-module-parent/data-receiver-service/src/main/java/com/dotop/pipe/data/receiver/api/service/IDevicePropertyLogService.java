package com.dotop.pipe.data.receiver.api.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.core.vo.device.DevicePropertyLogVo;

public interface IDevicePropertyLogService {

	void add(String deviceId, String deviceCode, String field, String tag, String name, String unit, String val,
			Date devSendDate, Date serReceDate, Date curr, String userBy, String enterpriseId);

	List<DevicePropertyLogVo> list(Date startDate, Date endDate);

	/**
	 * 动态插入数据接口
	 * @param originalData 
	 * @param deviceId 
	 * @param enterpriseId 
	 * 
	 * @param propertyMap
	 */
	void addProperty(Map<String, Object> propertyLogMap, String enterpriseId, String deviceId, JSONObject originalData);
}
