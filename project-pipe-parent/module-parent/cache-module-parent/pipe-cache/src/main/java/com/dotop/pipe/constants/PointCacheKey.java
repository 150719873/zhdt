package com.dotop.pipe.constants;

public class PointCacheKey {

    public final static String deviceProperty(String deviceId) {
        return "cache_point" + ":" + deviceId;
    }

    public final static String deviceCode(String deviceCode) {
        return "cache_device_code_exist" + ":" + deviceCode;
    }
}
