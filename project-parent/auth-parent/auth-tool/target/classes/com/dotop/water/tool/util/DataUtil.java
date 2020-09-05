package com.dotop.water.tool.util;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoginBo;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoginVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**

 * @date 2019年5月9日
 * @description 加解密工具类
 */
public final class DataUtil {

    private static final Logger LOGGER = LogManager.getLogger(DataUtil.class);

    private DataUtil() {

    }

    public static UserLoginVo getUserLogin(String dataString) {

        if (dataString == null) {
            return null;
        }

        String data = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
                dataString);
        UserLoginVo userLogin;
        try {
            userLogin = JSONUtils.parseObject(data, UserLoginVo.class);
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            return null;
        }
        return userLogin;
    }

    public static String generateCasKey(UserLoginBo userLogin) {
        if (userLogin == null) {
            return null;
        }
        return CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
                JSONUtils.toJSONString(userLogin));
    }
}
