package com.dotop.smartwater.project.module.cache.water;

public interface IWechatCache {

	void set(String type, String token, Object value);

	Object get(String type, String token);
}
