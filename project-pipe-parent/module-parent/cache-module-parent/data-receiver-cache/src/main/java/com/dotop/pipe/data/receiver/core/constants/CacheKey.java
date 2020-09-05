package com.dotop.pipe.data.receiver.core.constants;

public class CacheKey {

	public final static String deviceProperty(String deviceId) {
		return "cache_device_property" + ":" + deviceId;
	}

	public final static String deviceCode(String deviceCode) {
		return "cache_device_code_exist" + ":" + deviceCode;
	}
}
