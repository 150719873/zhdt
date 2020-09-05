package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.auth.api.IAccountFactory;
import com.dotop.smartwater.project.auth.api.IRoleFactory;
import com.dotop.smartwater.project.auth.cache.CBaseDao;
import com.dotop.smartwater.project.auth.cache.RedisDao;
import com.dotop.smartwater.project.auth.cache.UserCacheDao;
import com.dotop.smartwater.project.auth.common.FoundationController;
import com.dotop.smartwater.project.module.core.auth.constants.RevenueCode;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoginVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@RestController

@RequestMapping("/auth/water/test")
public class WaterTestController extends FoundationController implements BaseController<UserForm> {

	private static final Logger LOGGER = LogManager.getLogger(WaterTestController.class);

	@Resource
	private IAccountFactory iAccountFacoty;

	@Resource
	private UserCacheDao userCacheDao;

	@Resource
	private RedisDao redisDao;

	@Resource
	private IRoleFactory iRoleFactory;

	@Autowired
	private ISettlementService iSettlementService;

	@PostMapping(value = "/water_login", produces = GlobalContext.PRODUCES)
	public String waterLogin(@RequestBody UserForm user) {
		try {
			if (StringUtils.isBlank(user.getAccount()) || StringUtils.isBlank(user.getPassword())) {
				return resp(ResultCode.ParamIllegal, "账号或密码为空", null);
			}

			EnterpriseVo enterprise = iAccountFacoty.findEnterpriseById(user.getEnterpriseid());
			if (enterprise == null) {
				return resp(ResultCode.ParamIllegal, "没有配置水司", null);
			}
			if (enterprise.getFailureState() != null && enterprise.getFailureState() == 1) {
				return resp(ResultCode.ParamIllegal, "域名已失效", null);
			}
			if(enterprise.getFailureTime() != null && System.currentTimeMillis() > enterprise.getFailureTime().getTime()){
				return resp(ResultCode.ParamIllegal, "域名已失效", null);
			}
			user.setEnterpriseid(enterprise.getEnterpriseid());
			UserVo userLogin = iAccountFacoty.login(user.getEnterpriseid(), user.getAccount(), user.getPassword());
			if (userLogin == null) {
				return resp(ResultCode.AccountOrPasswordError, "账号或密码错误", null);
			}

			if (userLogin.getType() != UserVo.USER_TYPE_ADMIN) {
				if (enterprise.getProleid() == null) {
					return resp(ResultCode.ParamIllegal, "该水司没有初始化平台角色权限,请联系平台管理员配置", null);
				}
			}

			// 更新权限
			iRoleFactory.updateRolePermission(BeanUtils.copy(userLogin, UserForm.class), enterprise.getProleid());

			SettlementVo settlement = iSettlementService.getSettlement(user.getEnterpriseid());
			String ticket = String.valueOf(UUID.randomUUID());
			redisDao.setWebUserTicket(userLogin.getUserid(), ticket);

			userLogin.setEnterprise(enterprise);
			userLogin.setSettlement(settlement);
			userCacheDao.setUser(userLogin);

			UserLoginVo uLogin = new UserLoginVo();
			uLogin.setTicket(ticket);
			uLogin.setUserid(userLogin.getUserid());
			uLogin.setModelid(RevenueCode.Company_Revenue);
			return resp(ResultCode.Success, "Success", CBaseDao.generateCasKey(uLogin));
		} catch (Exception e) {
			LOGGER.error("water_login", e);
			return resp(ResultCode.Fail, "服务繁忙,请稍候再试", null);
		}
	}

}
