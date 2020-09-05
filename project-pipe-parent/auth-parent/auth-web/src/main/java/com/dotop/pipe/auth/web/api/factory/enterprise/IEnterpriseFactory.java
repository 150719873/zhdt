package com.dotop.pipe.auth.web.api.factory.enterprise;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

// 企业
public interface IEnterpriseFactory {

	// 初始化绑定认证中心企业eid与管道系统eid关联
	public void init(LoginCas loginCas) throws FrameworkRuntimeException;
}
