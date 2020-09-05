/** @author : KangJunRong
 *  @description : 
 *  @date : 2017年12月21日 上午10:08:48
 */
package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IUserFactory;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.form.UserLoginForm;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.third.form.iot.UserEntryForm;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;

/**
 * 
 * 用户控制
 * 

 * @date 2019年2月27日
 */
@RestController

@RequestMapping("/UserController")
public class UserController implements BaseController<BaseForm> {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserFactory iUserFactory;

	@PostMapping(value = "/logout", produces = GlobalContext.PRODUCES)
	public String logout(@RequestHeader("userid")String userid,@RequestHeader("ticket")String ticket) {
		logger.info(LogMsg.to("msg:", "logout开始"));
		iUserFactory.logout(userid,ticket);
		return resp(ResultCode.Success, "SUCCESS", null);
	}

	@PostMapping(value = "/login", produces = GlobalContext.PRODUCES)
	public String login(@RequestBody UserEntryForm userEntryForm) {
		logger.info(LogMsg.to("msg:", "查询logo功能开始"));
		// 校验字段
		String account = userEntryForm.getAccount();
		String password = userEntryForm.getPassword();
		VerificationUtils.string("account", account);
		VerificationUtils.string("password", password);
		String casKey = iUserFactory.login(userEntryForm);
		logger.info(LogMsg.to("msg:", "查询logo功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, casKey);
	}

	/**
	 * 认证中心过来的登录跳转
	 * 
	 * @param map
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/children_login", produces = GlobalContext.PRODUCES)
	public String childrenLogin(@RequestBody Map<String, String> map) {
		logger.info(LogMsg.to("msg:", "认证中心过来的登录跳转功能开始"));
		Map<String, Object> result = iUserFactory.childrenLogin(map);
		logger.info(LogMsg.to("msg:", "认证中心过来的登录跳转功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, result);
	}

	/**
	 * 获取CasKey
	 * 
	 * @param
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/getCasKey", produces = GlobalContext.PRODUCES)
	public String getCasKey(@RequestBody UserLoginForm userLogin) {
		logger.info(LogMsg.to("msg:", "getCasKey功能开始"));
		String modelid = userLogin.getModelid();
		String ticket = userLogin.getTicket();
		VerificationUtils.string("modelid", modelid);
		VerificationUtils.string("ticket", ticket);
		String casKey = iUserFactory.getCasKey(userLogin);
		logger.info(LogMsg.to("msg:", "getCasKey功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, casKey);
	}

	/**
	 * 检测用户是否失效
	 * 
	 * @param user
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/check_user_invalid", produces = GlobalContext.PRODUCES)
	public String checkUserInvalid(@RequestBody(required = false) UserForm user) {
		logger.info(LogMsg.to("msg:", " 检测用户是否失效 功能开始"));
		iUserFactory.checkUserInvalid(user);
		logger.info(LogMsg.to("msg:", " 检测用户是否失效 功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 获取所有企业
	 * 
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/login_enterprise", produces = GlobalContext.PRODUCES)
	public String loginEnterprise() {
		logger.info(LogMsg.to("msg:", "login_enterprise功能开始"));
		List<EnterpriseVo> list = iUserFactory.loginEnterprise();
		logger.info(LogMsg.to("msg:", "login_enterprise功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

}