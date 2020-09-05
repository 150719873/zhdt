package com.dotop.smartwater.project.auth.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@Component
public class UserCacheDao {

	private static final Logger LOGGER = LogManager.getLogger(UserCacheDao.class);
	private static final String KEY = "auth:user:";
	private static final int TIME = 60 * 60 * 24 * 30;

	@Autowired
	private StringValueCache svc;

	public void setUser(UserVo user) {
		try {
			if (user == null) {
				return;
			}
			svc.set(KEY + user.getUserid(), JSONUtils.toJSONString(user), TIME);
		} catch (Exception e) {
			LOGGER.error("setUser", e);
		}
	}

	public UserVo getUser(String userId) {
		try {
			return JSONUtils.parseObject(svc.get(KEY + userId), UserVo.class);
		} catch (Exception e) {
			LOGGER.error("getUser", e);
			return null;
		}
	}

	public void delUser(Long userId) {
		try {
			svc.del(KEY + userId);
		} catch (Exception e) {
			LOGGER.error("delUser", e);
		}
	}
}
