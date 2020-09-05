package com.dotop.pipe.auth.core.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;

public class AuthExceptionConstants extends BaseExceptionConstants {

	// 账户不存在
	public static final String ACCOUNT_NO_EXIST = "-990001";
	// 账户没有角色
	public static final String ACCOUNT_NO_ROLE = "-990002";
	// 账户认证失败
	public static final String ACCOUNT_AUTH_FAIL = "-990003";
	// 账户不在线
	public static final String ACCOUNT_OFFLINE = "-990004";
	// 账户被锁定
	public static final String ACCOUNT_USER_LOCK = "-990005";
	// 账户没有权限
	public static final String ACCOUNT_NO_AUTH = "-990006";
	// 账户票据过期
	public static final String ACCOUNT_TICKET_TIMEOUT = "-990007";
	// 账户权限点票据超时
	public static final String ACCOUNT_GET_PERMISSION_TOKEN_TIMEOUT = "-990008";

	public static final Map<String, String> msgMap = new HashMap<String, String>(getBaseMap()) {
		private static final long serialVersionUID = 6909788987793614246L;
		{
			put(ACCOUNT_NO_EXIST, "账户不存在");
			put(ACCOUNT_NO_ROLE, "账户没有角色");
			put(ACCOUNT_AUTH_FAIL, "账户认证失败");
			put(ACCOUNT_OFFLINE, "账户不在线");
			put(ACCOUNT_USER_LOCK, "账户被锁定");
			put(ACCOUNT_NO_AUTH, "账户没有权限");
			put(ACCOUNT_TICKET_TIMEOUT, "账户票据过期");
			put(ACCOUNT_GET_PERMISSION_TOKEN_TIMEOUT, "账户权限点票据超时");
		}
	};

	public final static String getMessage(String code, String... params) {
		String str = msgMap.get(code);
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				StringBuffer sb = new StringBuffer("{");
				sb.append(i).append("}");
				str = StringUtils.replace(str, sb.toString(), params[i]);
			}
		}
		return str;
	}
}
