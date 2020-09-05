package com.dotop.pipe.auth.web.api.factory.auth;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

// 权限
public interface IAuthFactory {

	// 登录鉴权
	public LoginCas authentication(String cas) throws FrameworkRuntimeException;

	// 登出
	public void loginOut() throws FrameworkRuntimeException;

	// 生成cas
	public String cas(String menuId) throws FrameworkRuntimeException;

	public String editMap(String mapType, List<String> protocols) throws FrameworkRuntimeException;

}
