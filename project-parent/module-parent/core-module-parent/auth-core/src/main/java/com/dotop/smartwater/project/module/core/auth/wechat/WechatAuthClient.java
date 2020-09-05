package com.dotop.smartwater.project.module.core.auth.wechat;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public class WechatAuthClient {

	private final static ThreadLocal<WechatUser> threadSession = new ThreadLocal<WechatUser>();

	public final static void add(WechatUser wechatUser) throws FrameworkRuntimeException {
		threadSession.set(wechatUser);
	}

	public final static WechatUser get() throws FrameworkRuntimeException {
		return threadSession.get();
	}
}
