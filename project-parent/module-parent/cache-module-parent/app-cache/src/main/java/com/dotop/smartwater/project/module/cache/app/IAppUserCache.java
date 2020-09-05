package com.dotop.smartwater.project.module.cache.app;

import com.dotop.smartwater.project.module.core.auth.vo.UserParamVo;

public interface IAppUserCache {

	/**
	 * @param userid
	 * @param value
	 */
	void setUserParamVo(String userid, UserParamVo value);

	/**
	 * @param userid
	 * @return
	 */
	UserParamVo getUserParamVo(String userid);
}
