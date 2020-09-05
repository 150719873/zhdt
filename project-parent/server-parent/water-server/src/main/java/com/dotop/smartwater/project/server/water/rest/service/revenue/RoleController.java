package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IRoleFactory;
import com.dotop.smartwater.project.module.core.auth.form.RoleForm;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.vo.MenuVo;

/**
 * @program: project-parent
 * @description: RoleController

 * @create: 2019-02-26 14:28
 **/
@RestController

@RequestMapping("/RoleController")
public class RoleController implements BaseController<RoleForm> {

	@Autowired
	private IRoleFactory iRoleFactory;

	@PostMapping(value = "/get_menu_child", produces = GlobalContext.PRODUCES)
	public String getMenuChild(@RequestBody RoleForm roleForm) {
		// 校验
		String parentid = roleForm.getParentid();
		VerificationUtils.string("parentid", parentid);

		// 数据封装
		List<MenuVo> list = iRoleFactory.getMenuChild(parentid);

		return resp(ResultCode.Success, ResultCode.SUCCESS, list);

	}

	@PostMapping(value = "/get_menu", produces = GlobalContext.PRODUCES)
	public String getMenu() {
		// 数据封装
		List<MenuVo> list = iRoleFactory.getMenu();
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);

	}
}
