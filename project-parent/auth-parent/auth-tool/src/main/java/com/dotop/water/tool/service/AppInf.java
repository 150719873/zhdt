package com.dotop.water.tool.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.water.tool.exception.BusinessException;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserParamVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.water.tool.util.HttpUtil;

/**

 * @date 2019年5月9日
 * @description app调用客户端
 */
public final class AppInf {

	private static final String USERID = "userid";

	private AppInf() {

	}

	public static boolean verification(String userid, String ticket) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put(USERID, userid);
			headers.put("ticket", ticket);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/app/api/verification", headers, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return Boolean.parseBoolean(parseObject.getString("data"));
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static UserParamVo appLogin(String account, String pwd, String eid) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("account", account);
			headers.put("pwd", pwd);
			headers.put("eid", eid);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/app/api/appLogin", headers, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), UserParamVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static List<EnterpriseVo> getAppEnterprises() throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			String key = UUID.randomUUID().toString();
			map.put("key", key);
			map.put("value", CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/app/api/enterpriseList", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseArray(parseObject.getString("data"), EnterpriseVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static UserVo getUserBaseInfo(String userid, String ticket) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, userid);
			map.put("ticket", ticket);
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/app/api/getUserBaseInfo", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), UserVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static UserVo getUserInfo(String userid) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put(USERID, userid);

			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put("value", CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1,
					WaterClientConfig.WaterCasKey2, JSONUtils.toJSONString(key)));

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/app/api/getUserInfo", headers, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), UserVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}
}
