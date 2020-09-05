package com.dotop.smartwater.project.third.module.core.utils;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.MD5Util;

import java.util.Date;


/**
 *
 */
public class SaltUtils {



    public static String getOwnerSalt(String UserNo, String username, String userAddr, String json, String deviceId) throws FrameworkRuntimeException {
        StringBuilder stringBuilder = new StringBuilder().append(UserNo).append(username).append(userAddr).append(json).append(deviceId);
        return MD5Util.encode(stringBuilder.toString());
    }

    public static String getDeviceSalt(String addr, String json, String eui, String no) throws FrameworkRuntimeException {
        StringBuilder stringBuilder = new StringBuilder().append(eui).append(addr).append(json).append(no);
        return MD5Util.encode(stringBuilder.toString());
    }

    public static String getRecordSalt(Date uplinkDate, String json, Double water, String eui, String devId) throws FrameworkRuntimeException {
        StringBuilder stringBuilder = new StringBuilder().append(eui).append(json).append(uplinkDate).append(water).append(devId);
        return MD5Util.encode(stringBuilder.toString());
    }

    public static String getNBDeviceSalt(String agreement, Integer delivery, Integer tapstatus, String eui) throws FrameworkRuntimeException {
        StringBuilder stringBuilder = new StringBuilder().append(eui).append(agreement).append(delivery).append(tapstatus);
        return MD5Util.encode(stringBuilder.toString());
    }



}
