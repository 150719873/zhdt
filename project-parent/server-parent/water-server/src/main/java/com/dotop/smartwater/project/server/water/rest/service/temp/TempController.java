package com.dotop.smartwater.project.server.water.rest.service.temp;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.water.tool.service.BaseInf;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 

 * 
 * 
 */
@RestController

@RequestMapping("/watercas")
public class TempController implements BaseController<BaseForm> {

	/**
	 * 原com.dotop.water.controller.CasController
	 */
	@PostMapping(value = "/areas", produces = GlobalContext.PRODUCES)
	public String areas() {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();

		List<AreaNodeVo> list = BaseInf.getAreaList(user.getUserid(), user.getTicket());
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);

	}

	@PostMapping(value = "/getUserListByEnterpriseId", produces = GlobalContext.PRODUCES)
	public String getUserListByEnterpriseId() {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();

		List<UserVo> list = BaseInf.getUserList(user.getUserid(), user.getTicket(),user.getEnterpriseid());
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);

	}
}
