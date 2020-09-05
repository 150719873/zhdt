package com.dotop.smartwater.dependence.core.utils;

import java.util.UUID;

/**

 * @date 2019年5月8日
 * @description uuid工具类
 */
public class UuidUtils {

    private UuidUtils() {
        super();
    }

    public static final String getUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 不要横杠
     */
    public static final String getUuid(boolean flag) {
        UUID uuid = UUID.randomUUID();
        if (flag) {
            return uuid.toString().replace("-", "");
        }
        return uuid.toString();
    }
}
