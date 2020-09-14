package com.dotop.smartwater.project.third.concentrator.core.utils;

import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * 集中器工具类
 */
public final class ConcentratorUtils {

    public static String getCollectorCode(String collectorCode, String deviceChannel) {
        if (StringUtils.isBlank(collectorCode) || StringUtils.isBlank(deviceChannel) || collectorCode.endsWith(ConcentratorConstants.COLLECTOR_ABSTRACT)) {
            return collectorCode;
        }
        // 20为协议；长度为6个字节
        StringBuilder sb = new StringBuilder(collectorCode);
        sb.deleteCharAt(collectorCode.length() - 1);
        sb.append(deviceChannel);
        if (sb.length() == 12) {
            return sb.toString();
        }
        return null;
    }

    public static String getDeviceCode(String deviceCode) {
        StringBuilder devno = new StringBuilder(deviceCode);
        String q = devno.substring(0, 4);
        if (q.startsWith("0000")) {
            q = "";
        } else if (q.startsWith("000")) {
            q = devno.substring(3, 4);
        } else if (q.startsWith("00")) {
            q = devno.substring(2, 4);
        } else if (q.startsWith("0")) {
            q = devno.substring(1, 4);
        } else {
            q = devno.substring(0, 4);
        }
        String f = devno.substring(4, 12);
        return q + f;
    }

}
