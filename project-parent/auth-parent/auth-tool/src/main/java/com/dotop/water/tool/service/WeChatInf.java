package com.dotop.water.tool.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.water.tool.exception.BusinessException;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.water.tool.util.HttpUtil;

@Deprecated
public class WeChatInf {

	public static WechatPublicSettingVo getWechatPublicSetting(String enterpriseid) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put("value", CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1,
					WaterClientConfig.WaterCasKey2, JSONUtils.toJSONString(key)));
			headers.put("enterpriseid", String.valueOf(enterpriseid));
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/getWeChatPublicSetting",
					headers, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), WechatPublicSettingVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut);
		} catch (BusinessException e) {
			throw e;
		} catch (Throwable e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

}
