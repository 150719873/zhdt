package com.dotop.water.tool.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dotop.water.tool.exception.BusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.TypeReference;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserInfoVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.MenuBo;
import com.dotop.smartwater.project.module.core.water.vo.MenuVo;
import com.dotop.water.tool.util.HttpUtil;

/**

 * @date 2019年5月9日
 * @description 调用客户端
 */
public final class BaseInf {

	private static final Logger LOGGER = LogManager.getLogger(BaseInf.class);

	private static final String USERID = "userid";
	private static final String TICKET = "ticket";

	private BaseInf() {

	}

	/**
	 * @param userid
	 *            用户id
	 * @param ticket
	 *            票据
	 * @param mid
	 *            权限码
	 * @return 1 通过 0 失败
	 * @throws BusinessException
	 */
	public static Integer webIsPass(String userid, String ticket, String mid) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, userid);
			map.put(TICKET, ticket);
			map.put("mid", mid);
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/api/permission", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return parseObject.getInteger("data");
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static Map<String, EnterpriseVo> getEnterpriseMaps(String userid, String ticket) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, userid);
			map.put(TICKET, ticket);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/api/enterpriseMap", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), new TypeReference<Map<String, EnterpriseVo>>() {
			});

		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	/**
	 * @param account
	 *            账号
	 * @param pwd
	 *            密码
	 * @param website
	 *            水司域名
	 * @return 用户Json
	 * @throws BusinessException
	 */
	public static String webLogin(String account, String pwd, String website) throws BusinessException {
		try {
			UserBo user = new UserBo();
			user.setAccount(account);
			user.setPassword(pwd);
			user.setWebsite(website);
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/UserController/login", null,
					JSONUtils.toJSONString(user).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return parseObject.getString("data");
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	/**
	 * @param userid
	 *            用户Id
	 * @param ticket
	 *            票据
	 * @param modelId
	 *            项目ID
	 * @return 返回菜单列表
	 * @throws BusinessException
	 */
	public static UserInfoVo getUserInfo(String userid, String ticket, String modelId) throws BusinessException {
		try {
			MenuBo menu = new MenuBo();
			menu.setModelid(modelId);

			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, userid);
			map.put(TICKET, ticket);
			map.put("modelid", modelId);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/api/user", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), UserInfoVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	/**
	 * 根据多个用户ID查询用户信息
	 * 
	 * @param userid
	 *            用户Id
	 * @param ticket
	 *            票据
	 * @param modelId
	 *            项目ID
	 * @return 返回菜单列表
	 * @throws BusinessException
	 */
	public static List<UserVo> getUsers(String userid, String ticket, String userIds) throws BusinessException {
		try {

			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, userid);
			map.put(TICKET, ticket);
			map.put("userIds", String.valueOf(userIds));

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/api/getUsers", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseArray(parseObject.getString("data"), UserVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	/**
	 * @param userid
	 * @param ticket
	 * @return false/true
	 * @throws BusinessException
	 */
	public static boolean webLogout(String userid, String ticket) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, userid);
			map.put(TICKET, ticket);
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/UserController/logout", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return true;
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	/**
	 * @param userid
	 * @param ticket
	 * @return 区域列表
	 * @throws BusinessException
	 */
	public static List<AreaNodeVo> getAreaList(String userid, String ticket) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, userid);
			map.put(TICKET, ticket);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/api/getCompanyAreas", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseArray(parseObject.getString("data"), AreaNodeVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}
	/**
	 * @param userid
	 * @param ticket
	 * @return 菜单列表
	 * @throws BusinessException
	 */
	public static List<MenuVo> getMenu(String userid, String ticket) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, userid);
			map.put(TICKET, ticket);

			MenuBo params = new MenuBo();

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/RoleController/get_menu", map,
					JSONUtils.toJSONString(params).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseArray(parseObject.getString("data"), MenuVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}
	/**
	 * @param userid
	 * @param ticket
	 * @param modelid
	 * @return 菜单列表
	 * @throws BusinessException
	 */
	public static List<MenuVo> getMenuChild(String userid, String ticket, String modelid) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, userid);
			map.put(TICKET, ticket);

			MenuBo params = new MenuBo();
			params.setParentid(modelid);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/RoleController/get_menu_child", map,
					JSONUtils.toJSONString(params).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseArray(parseObject.getString("data"), MenuVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	/**
	 * @param userid
	 * @return 获取areaMap
	 * @throws BusinessException
	 */
	public static Map<String, AreaNodeVo> getAreaMaps(String userid, String ticket) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, userid);
			map.put(TICKET, ticket);

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/api/getAreaMaps", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), new TypeReference<Map<String, AreaNodeVo>>() {
			});

		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static EnterpriseVo getCompanyInfo(Long eid) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			String key = UUID.randomUUID().toString();
			map.put("key", key);
			map.put("eid", String.valueOf(eid));
			map.put("value", CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/api/getCompanyInfo", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), EnterpriseVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static AreaVo getAreaById(String cid) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			String key = UUID.randomUUID().toString();
			map.put("key", key);
			map.put("cid", String.valueOf(cid));
			map.put("value", CipherEncryptors.encrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
					JSONUtils.toJSONString(key)));

			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/api/getAreaById", map, null);
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseObject(parseObject.getString("data"), AreaVo.class);
		} catch (IOException e) {
			throw new BusinessException(AuthResultCode.TimeOut, e);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static List<UserVo> getUserList(String userid, String ticket, String enterpriseid) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, String.valueOf(userid));
			map.put(TICKET, ticket);

			UserBo user = new UserBo();
			user.setEnterpriseid(enterpriseid);
			user.setPage(1);
			user.setPageCount(1000);
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/UserController/get_user", map,
					JSONUtils.toJSONString(user).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseArray(parseObject.getString("data"), UserVo.class);
		} catch (Exception e) {
			LOGGER.error(LogMsg.to("ex", e, "BaseInf->getUserList", e.getMessage()));
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static List<UserVo> getUserList(String enterpriseid) {
		try {
			HashMap<String, String> map = new HashMap<>();
			UserBo user = new UserBo();
			user.setEnterpriseid(enterpriseid);
			user.setPage(1);
			user.setPageCount(1000);
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/UserController/getUserList", map,
					JSONUtils.toJSONString(user).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseArray(parseObject.getString("data"), UserVo.class);
		} catch (Exception e) {
			LOGGER.error(LogMsg.to("ex", e, "BaseInf->getUserList", e.getMessage()));
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}

	public static List<UserVo> getUserList(String userid, String ticket, UserBo user) throws BusinessException {
		try {
			HashMap<String, String> map = new HashMap<>();
			map.put(USERID, String.valueOf(userid));
			map.put(TICKET, ticket);

			user.setPage(1);
			user.setPageCount(99999);
			String data = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/UserController/get_user", map,
					JSONUtils.toJSONString(user).getBytes(StandardCharsets.UTF_8));
			JSONObjects parseObject = JSONUtils.parseObject(data);
			String code = parseObject.getString("code");
			if (!AuthResultCode.Success.equals(code)) {
				throw new BusinessException(code);
			}
			return JSONUtils.parseArray(parseObject.getString("data"), UserVo.class);
		} catch (Exception e) {
			LOGGER.error(LogMsg.to("ex", e, "BaseInf->getUserList", e.getMessage()));
			throw new BusinessException(AuthResultCode.Fail, e.getMessage(), e);
		}
	}
}
