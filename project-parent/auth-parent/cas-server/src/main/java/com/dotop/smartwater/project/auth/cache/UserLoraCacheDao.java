package com.dotop.smartwater.project.auth.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@Component
public class UserLoraCacheDao {

	private static final Logger LOGGER = LogManager.getLogger(UserLoraCacheDao.class);
	private static final String KEY = "auth:userLora:";
	private static final int TIME = 60 * 60 * 24 * 30;

	@Autowired
	private StringValueCache svc;

	public void setUserLora(UserLoraVo userLora) {
		try {
			if (userLora == null) {
				return;
			}
			svc.set(KEY + userLora.getEnterpriseid(), JSONUtils.toJSONString(userLora), TIME);
		} catch (Exception e) {
			LOGGER.error("setUser", e);
		}
	}

	public UserLoraVo getUserLora(String eid) {
		try {
			return JSONUtils.parseObject(svc.get(KEY + eid), UserLoraVo.class);
		} catch (Exception e) {
			LOGGER.error("getUserLora", e);
			return null;
		}
	}
}
