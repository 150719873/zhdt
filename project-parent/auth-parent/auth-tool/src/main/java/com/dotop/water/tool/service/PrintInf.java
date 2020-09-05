package com.dotop.water.tool.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.water.tool.exception.BusinessException;
import com.dotop.water.tool.util.HttpUtil;
import com.dotop.smartwater.project.module.core.auth.bo.BindParamBo;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.smartwater.project.module.core.auth.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.auth.vo.PrintVo;
import com.dotop.smartwater.project.module.core.water.bo.DesignPrintBo;
import com.dotop.smartwater.project.module.core.water.vo.customize.DesignFieldVo;

/**

 * @date 2019年5月9日
 * @description 调用客户端
 */
public final class PrintInf {

	private static final String VALUE = "value";

	private PrintInf() {

	}

	public static PrintVo getPrintStatus(String enterpriseid, Integer smstype) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			BindParamBo bp = new BindParamBo();
			bp.setEnterpriseid(enterpriseid);
			bp.setSmstype(smstype);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/getPrintStatus", headers,
					JSONUtils.toJSONString(bp).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), PrintVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static List<DesignPrintVo> getDesignPrintList(String condition) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			DesignPrintBo obj = new DesignPrintBo();
			obj.setSqlstr(condition);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/getDesignPrintList", headers,
					JSONUtils.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseArray(parseObject.getString("data"), DesignPrintVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static Long getDesignPrintCount(String condition) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			DesignPrintBo obj = new DesignPrintBo();
			obj.setSqlstr(condition);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/getDesignPrintCount", headers,
					JSONUtils.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return parseObject.getLong("data");
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static DesignPrintVo getDesignPrint(String id) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			DesignPrintBo obj = new DesignPrintBo();
			obj.setId(id);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/getDesignPrint", headers,
					JSONUtils.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), DesignPrintVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static Long deleteDesignPrint(String id) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			DesignPrintBo obj = new DesignPrintBo();
			obj.setId(id);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/deleteDesignPrint", headers,
					JSONUtils.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return parseObject.getLong("data");
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static List<DesignFieldVo> getDesignFields(String id) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			DesignPrintBo obj = new DesignPrintBo();
			obj.setId(id);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/getDesignFields", headers,
					JSONUtils.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseArray(parseObject.getString("data"), DesignFieldVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static Long deleteDesignFields(String id) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			DesignPrintBo obj = new DesignPrintBo();
			obj.setId(id);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/deleteDesignFields", headers,
					JSONUtils.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return parseObject.getLong("data");
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static boolean updateDesignPrint(DesignPrintBo obj) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/updateDesignPrint", headers,
					JSONUtils.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return parseObject.getBoolean("data");
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static boolean saveDesignPrint(DesignPrintBo obj) throws BusinessException {
		try {
			HashMap<String, String> headers = new HashMap<>();
			String key = UUID.randomUUID().toString();
			headers.put("key", key);
			headers.put(VALUE, CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/tool/api/saveDesignPrint", headers,
					JSONUtils.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return parseObject.getBoolean("data");
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

}
