package com.dotop.pipe.auth.core.constants;

public class CacheKey {

	// 用户为key，票据为val
	public final static String ut(String userId) {
		return "cache_user_token" + ":" + userId;
	}

	// 票据为key，用户为val
	public final static String tu(String token) {
		return "cache_token_user" + ":" + token;
	}

	public final static String e(String eid) {
		return "cache_enterprise" + ":" + eid;
	}
}
