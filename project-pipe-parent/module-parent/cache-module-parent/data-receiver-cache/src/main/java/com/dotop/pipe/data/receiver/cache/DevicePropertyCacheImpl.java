package com.dotop.pipe.data.receiver.cache;

import java.util.concurrent.TimeUnit;

import com.dotop.pipe.data.receiver.api.cache.IDevicePropertyCache;
import com.dotop.pipe.data.receiver.core.constants.CacheKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;

@Component
public class DevicePropertyCacheImpl extends IDevicePropertyCache { // BaseCache implements

	@Value("${device.property.expire:600}")
	protected long devicePropertyExpire;

	@Override
	public DevicePropertyVo hashGet(String deviceId, String field) {
		String str = cacheOpsForHash.get(CacheKey.deviceProperty(deviceId), field);
		if (StringUtils.isNotBlank(str)) {
			return JSONUtils.parseObject(str, DevicePropertyVo.class);
		}
		return null;
	}

	@Override
	public void set(DevicePropertyVo deviceProperty) throws FrameworkRuntimeException {
		String deviceId = deviceProperty.getDeviceId();
		String field = deviceProperty.getField();
		String str = JSONUtils.toJSONString(deviceProperty);
		cacheOpsForHash.put(CacheKey.deviceProperty(deviceId), field, str);
		cacheRedisTemplate.expire(CacheKey.deviceProperty(deviceId), devicePropertyExpire, TimeUnit.SECONDS);
	}

	@Override
	public boolean getByDeviceCode(String deviceCode) throws FrameworkRuntimeException {
		String flag = cacheOpsForValue.get(CacheKey.deviceCode(deviceCode));
		if (StringUtils.isNotBlank(flag)) {
			return true;
		}
		return false;
	}

	@Override
	public void setByDeviceCode(String deviceCode) throws FrameworkRuntimeException {
		cacheOpsForValue.set(CacheKey.deviceCode(deviceCode), deviceCode, devicePropertyExpire, TimeUnit.SECONDS);
	}

}
