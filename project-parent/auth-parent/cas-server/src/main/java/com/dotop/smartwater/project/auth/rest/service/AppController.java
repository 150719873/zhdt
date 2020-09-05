package com.dotop.smartwater.project.auth.rest.service;

import java.util.Map;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.dotop.smartwater.project.auth.api.IAccountFactory;
import com.dotop.smartwater.project.auth.api.IInfFactory;
import com.dotop.smartwater.project.auth.cache.CBaseDao;
import com.dotop.smartwater.project.auth.cache.RedisDao;
import com.dotop.smartwater.project.auth.cache.UserCacheDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserParamVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.auth.vo.select.Obj;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@RestController

@RequestMapping("/auth/app/api")
public class AppController implements BaseController<UserForm> {

	private static final Logger LOGGER = LogManager.getLogger(AppController.class);

	@Resource
	private IInfFactory iInfFactory;

	@Resource
	private IAccountFactory iAccountFactory;

	@Resource
	private RedisDao redisDao;

	@Resource
	private CBaseDao baseDao;

	@Resource
	private UserCacheDao userCacheDao;

	@Autowired
	private ISettlementService iSettlementService;

	@PostMapping(value = "/enterpriseList", produces = GlobalContext.PRODUCES)
	public String enterpriseList(@RequestHeader("key") String key, @RequestHeader("value") String value) {
		String dKey = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2, value);

		if (!key.equals(JSONUtils.parseObject(dKey, String.class))) {
			return resp(AuthResultCode.Fail, "不是合法请求", null);
		}
		return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, iInfFactory.getErpList());
	}

	@PostMapping(value = "/appLogin", produces = GlobalContext.PRODUCES)
	public String appLogin(@RequestHeader("eid") String eid, @RequestHeader("account") String account,
			@RequestHeader("pwd") String pwd) {

		UserVo userLogin = iAccountFactory.login(eid, account, pwd);
		if (userLogin == null) {
			return resp(AuthResultCode.AccountOrPasswordError, "account or password error", null);
		}

		String ticket = UuidUtils.getUuid();
		redisDao.setWaterAppUserTicket(userLogin.getUserid(), ticket);

		UserParamVo userParamVo = new UserParamVo();
		BeanUtils.copyProperties(userLogin, userParamVo);
		userParamVo.setTicket(ticket);

		//【先在缓存里找，再到数据库里找】
		EnterpriseVo enterprise = iAccountFactory.findEnterpriseById(userLogin.getEnterpriseid());
		if (enterprise != null) {
			userParamVo.setCalibration(enterprise.getCalibration());
		}

		return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, userParamVo);
	}

	@PostMapping(value = "/verification", produces = GlobalContext.PRODUCES)
	public String verification(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
		//【第三个参数是从缓存中拿到的，应该就是上面方法中对应的88行】
		return resp(AuthResultCode.Success, "Success", baseDao.waterAppAuth(userid, ticket));
	}

	@PostMapping(value = "/getUserBaseInfo", produces = GlobalContext.PRODUCES)
	public String getUserBaseInfo(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.AccountInvalid, "验证失败,请重新登录", null);
		}
		return resp(AuthResultCode.Success, "Success", user);
	}

	@PostMapping(value = "/getUserInfo", produces = GlobalContext.PRODUCES)
	public String getUserInfo(@RequestHeader("key") String key, @RequestHeader("value") String value,
			@RequestHeader("userid") String userid) {
		String dKey = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2, value);
		if (!key.equals(JSONUtils.parseObject(dKey, String.class))) {
			return resp(AuthResultCode.Fail, "不是合法请求", null);
		}
		UserVo user = baseDao.getRedisUser(userid);
		return resp(AuthResultCode.Success, "Success", user);
	}

	@PostMapping(value = "/workLogin", produces = GlobalContext.PRODUCES)
	public String workLogin(@RequestHeader("website") String website, @RequestHeader("userno") String userno,
			@RequestHeader("pwd") String pwd) {

		if (StringUtils.isBlank(userno) || StringUtils.isBlank(pwd)) {
			return resp(AuthResultCode.ParamIllegal, "账号或密码为空", null);
		}
		EnterpriseVo enterprise = iAccountFactory.findEnterpriseByWebsite(website);
		if (enterprise == null) {
			return resp(AuthResultCode.ParamIllegal, "域名没有配置", null);
		}
		UserVo userLogin = iAccountFactory.login(enterprise.getEnterpriseid(), userno, pwd);
		if (userLogin == null) {
			return resp(AuthResultCode.AccountOrPasswordError, "账号或密码错误", null);
		}
		String ticket = redisDao.getWebUserTicket(userLogin.getUserid());
		if (StringUtils.isBlank(ticket)) {
			ticket = UuidUtils.getUuid();
			redisDao.setWebUserTicket(userLogin.getUserid(), ticket);
		}
		SettlementVo settlement = iSettlementService.getSettlement(enterprise.getEnterpriseid());
		userLogin.setEnterprise(enterprise);
		userLogin.setSettlement(settlement);
		userCacheDao.setUser(userLogin);

		UserParamVo userParamVo = new UserParamVo();
		BeanUtils.copyProperties(userLogin, userParamVo);
		userParamVo.setTicket(ticket);

		return resp(AuthResultCode.Success, "Success", userParamVo);
	}

	/**
	 * 获取人员组织架构名称(功能同 InterfaceController.getOrganizationChart,为app提供)
	 */
	@PostMapping(value = "/getOrganizationChart", produces = GlobalContext.PRODUCES)
	public String getOrganizationChart(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(ResultCode.TimeOut, "验证失败,请重新登录", null);
		}
		try {
			Map<String, Obj> map = iInfFactory.getOrganizationChartMap(user.getEnterpriseid());
			return resp(ResultCode.Success, "Success", map);
		} catch (Exception e) {
			LOGGER.error("getOrganizationChart", e);
			return resp(ResultCode.Fail, e.getMessage(), null);
		}
	}

}
