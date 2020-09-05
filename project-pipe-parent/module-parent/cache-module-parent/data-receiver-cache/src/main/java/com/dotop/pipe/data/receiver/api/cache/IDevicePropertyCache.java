package com.dotop.pipe.data.receiver.api.cache;

import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.smartwater.dependence.cache.api.AbstractBaseCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public abstract class IDevicePropertyCache extends AbstractBaseCache<String, DevicePropertyVo, String> {

	public abstract DevicePropertyVo hashGet(String deviceId, String field) throws FrameworkRuntimeException;

	public abstract void set(DevicePropertyVo deviceProperty) throws FrameworkRuntimeException;

	public abstract boolean getByDeviceCode(String deviceCode) throws FrameworkRuntimeException;

	public abstract void setByDeviceCode(String deviceCode) throws FrameworkRuntimeException;

}
