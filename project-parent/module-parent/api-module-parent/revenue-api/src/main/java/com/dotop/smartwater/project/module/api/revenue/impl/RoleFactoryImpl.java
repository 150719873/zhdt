package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.revenue.IRoleFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.vo.MenuVo;
import com.dotop.water.tool.service.BaseInf;

/**
 * 用户角色
 *

 * @date 2019年2月25日
 */
@Component
public class RoleFactoryImpl implements IRoleFactory {

	@Override
	public List<MenuVo> getMenuChild(String parentid) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		return BaseInf.getMenuChild(user.getUserid(), user.getTicket(), parentid);
	}

	@Override
	public List<MenuVo> getMenu() {
		UserVo user = AuthCasClient.getUser();
		return BaseInf.getMenu(user.getUserid(), user.getTicket());
	}
}
