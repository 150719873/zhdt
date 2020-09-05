package com.dotop.smartwater.project.module.cache.app.impl;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.project.module.cache.app.IAppUserCache;
import com.dotop.smartwater.project.module.core.auth.vo.UserParamVo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppUserCacheImpl implements IAppUserCache {

	@Autowired
	protected AbstractValueCache<UserParamVo> avc;

	@Override
	public void setUserParamVo(String userid, UserParamVo value) {
		if (value == null) {
			return;
		}
		avc.set(CacheKey.AppUserKey + userid, value, Config.APP_LIVE_TIME);
	}

	@Override
	public UserParamVo getUserParamVo(String userid) {
		return avc.getVo(CacheKey.AppUserKey + userid, UserParamVo.class);
	}

}
