package com.dotop.water.tool.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.water.tool.exception.BusinessException;
import com.dotop.smartwater.project.module.core.auth.bo.MessageParamBo;
import com.dotop.smartwater.project.module.core.auth.bo.SendMsgBo;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.water.tool.util.HttpUtil;

/**

 * @date 2019年5月9日
 * @description 调用客户端
 */
public final class SendMsgInf {

	private static final String VALUE = "value";

	private SendMsgInf() {

	}

	public static Integer sendSms(SendMsgBo sendMsgBo) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/sendSms", headers,
					JSONUtils.toJSONString(sendMsgBo).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			return parseObject.getInteger("code");
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static Integer sendWeChatMsg(SendMsgBo sendMsgBo) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/sendWeChatMsg", headers,
					JSONUtils.toJSONString(sendMsgBo).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			return parseObject.getInteger("code");
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static Integer sendMessage(MessageParamBo params) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/sendMessage", headers,
					JSONUtils.toJSONString(params).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			return parseObject.getInteger("code");
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}
}
