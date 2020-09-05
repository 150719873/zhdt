package com.dotop.smartwater.view.server.rest.service.auth;

import com.dotop.pipe.auth.core.form.AuthForm;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.auth.web.api.factory.auth.IAuthFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 
 *
 * @date 2019年1月16日
 */
@RestController
@RequestMapping("/auth")
public class AuthController implements BaseController<AuthForm> {

	private final static Logger logger = LogManager.getLogger(AuthController.class);

	@Autowired
	private IAuthFactory iAuthFactory;

	/**
	 * 鉴权
	 */
	@PostMapping(value = "/authentication", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String authentication(@RequestBody AuthForm authForm) {
		logger.info(LogMsg.to("authForm", authForm));
		String cas = authForm.getCas();
		// 验证
		VerificationUtils.string("cas", cas, false, 500);
		LoginCas loginCas = iAuthFactory.authentication(cas);
		return resp("loginCas", loginCas);
	}

	/**
	 * 登出
	 */
	@PostMapping(value = "/loginOut", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String loginOut() {
		iAuthFactory.loginOut();
		return resp();
	}

	/**
	 * 校验是否存在
	 */
	@PostMapping(value = "/isOnline", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String isOnline() {
		return resp();
	}

	/**
	 * 校验是否存在
	 */
	@PostMapping(value = "/cas", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String cas(@RequestBody AuthForm authForm) {
		String modelId = authForm.getModelId();
		VerificationUtils.string("modelId", modelId);
		String cas = iAuthFactory.cas(modelId);
		return resp("cas", cas);
	}

}
