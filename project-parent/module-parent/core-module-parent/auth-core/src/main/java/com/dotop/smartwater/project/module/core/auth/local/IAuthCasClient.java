package com.dotop.smartwater.project.module.core.auth.local;

import java.util.Date;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

public interface IAuthCasClient {

	default LocalObj get() throws FrameworkRuntimeException {
		return AuthCasClient.get();
	}

	default UserVo getUser() throws FrameworkRuntimeException {
		return AuthCasClient.get().getUser();
	}

	default UserBo getUserBo() throws FrameworkRuntimeException {
		return BeanUtils.copy(getUser(), UserBo.class);
	}

	default String getUserid() throws FrameworkRuntimeException {
		return getUser().getUserid();
	}

	default String getRoleid() throws FrameworkRuntimeException {
		return getUser().getRoleid();
	}

	default String getEnterpriseid() throws FrameworkRuntimeException {
		return getUser().getEnterpriseid();
	}

	default String getName() throws FrameworkRuntimeException {
		return getUser().getName();
	}

	default String getAccount() throws FrameworkRuntimeException {
		return getUser().getAccount();
	}

	default Date getCurr() throws FrameworkRuntimeException {
		return AuthCasClient.getCurr();
	}

	default Integer getUsertype() throws FrameworkRuntimeException {
		return AuthCasClient.getUser().getType();
	}
}
