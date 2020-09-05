package com.dotop.pipe.auth.api.cache.auth;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.cache.api.AbstractBaseCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

// 权限
public abstract class IAuthCache extends AbstractBaseCache<String, LoginCas, String> {

	// 新增
	public abstract String set(LoginCas loginCas) throws FrameworkRuntimeException;

	// 获取
	public abstract LoginCas get(String userId) throws FrameworkRuntimeException;

	// 清空
	public abstract void del(LoginCas loginCas) throws FrameworkRuntimeException;

}
