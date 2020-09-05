package com.dotop.smartwater.project.auth.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@Component
public class EnterpriseCacheDao {

	private static final Logger LOGGER = LogManager.getLogger(EnterpriseCacheDao.class);

	private static final String KEY = "auth:enterprise:";
	private static final int TIME = 60 * 60 * 24 * 30;

	@Autowired
	private StringValueCache svc;

	public void setEnterprise(EnterpriseVo enterprise) {
		try {
			if (enterprise == null) {
				return;
			}
			svc.set(KEY + enterprise.getEnterpriseid(), JSONUtils.toJSONString(enterprise), TIME);
		} catch (Exception e) {
			LOGGER.error("setEnterprise", e);
		}
	}

	public EnterpriseVo getEnterprise(String enterpriseId) {
		try {
			return JSONUtils.parseObject(svc.get(KEY + enterpriseId), EnterpriseVo.class);
		} catch (Exception e) {
			LOGGER.error("getEnterprise", e);
			return null;
		}
	}

	public void delEnterprise(String enterpriseId) {
		try {
			svc.del(KEY + enterpriseId);
		} catch (Exception e) {
			LOGGER.error("delEnterprise", e);
		}
	}
}
