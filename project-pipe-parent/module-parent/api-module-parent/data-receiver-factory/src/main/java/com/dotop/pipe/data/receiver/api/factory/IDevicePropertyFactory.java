package com.dotop.pipe.data.receiver.api.factory;

import java.util.List;

import com.dotop.pipe.data.receiver.core.model.DeviceRD;
import com.dotop.pipe.data.receiver.core.vo.AlarmDesVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface IDevicePropertyFactory {

	public List<AlarmDesVo> merge(DeviceVo device, DeviceRD deviceRD) throws FrameworkRuntimeException;

	/**
	 * 查询缓存
	 * 
	 * @param deviceCode
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	public boolean getCacheByDeviceCode(String deviceCode) throws FrameworkRuntimeException;

	/**
	 * 添加缓存
	 * 
	 * @param deviceCode
	 * @param flag
	 * @throws FrameworkRuntimeException
	 */
	public void setCacheByDeviceCode(String deviceCode) throws FrameworkRuntimeException;

}
