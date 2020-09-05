package com.dotop.smartwater.project.module.core.auth.local;

import java.util.Date;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

public class AuthCasClient {

	private final static ThreadLocal<LocalObj> threadSession = new ThreadLocal<LocalObj>();

	public final static void add(UserVo user) throws FrameworkRuntimeException {
		LocalObj obj = new LocalObj();
		obj.setUser(user);
		obj.setCurr(new Date());
		threadSession.set(obj);
	}

	public final static LocalObj get() throws FrameworkRuntimeException {
		return threadSession.get();
	}

	public final static UserVo getUser() throws FrameworkRuntimeException {
		if (threadSession.get() != null) {
			return threadSession.get().getUser();
		}
		// 非空检验
		throw new FrameworkRuntimeException(AuthResultCode.UserNotLogin, "用户没登录");
	}

	public final static String getUserid() throws FrameworkRuntimeException {
		if (threadSession.get() != null) {
			return getUser().getUserid();
		}
		throw new FrameworkRuntimeException(AuthResultCode.UserNotLogin, "用户没登录");
	}

	public final static String getEnterpriseid() throws FrameworkRuntimeException {
		if (threadSession.get() != null) {
			return getUser().getEnterpriseid();
		}
		throw new FrameworkRuntimeException(AuthResultCode.UserNotLogin, "用户没登录");
	}

	public final static String getName() throws FrameworkRuntimeException {
		if (threadSession.get() != null) {
			return getUser().getName();
		}
		throw new FrameworkRuntimeException(AuthResultCode.UserNotLogin, "用户没登录");
	}

	public final static String getAccount() throws FrameworkRuntimeException {
		if (threadSession.get() != null) {
			return getUser().getAccount();
		}
		throw new FrameworkRuntimeException(AuthResultCode.UserNotLogin, "用户没登录");
	}

	public final static Date getCurr() throws FrameworkRuntimeException {
		return get().getCurr();
	}
}
