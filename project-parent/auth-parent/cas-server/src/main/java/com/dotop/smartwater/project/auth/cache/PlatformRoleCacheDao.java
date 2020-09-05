package com.dotop.smartwater.project.auth.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.vo.RolePermissionVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@Component
public class PlatformRoleCacheDao {

	private static final Logger LOGGER = LogManager.getLogger(PlatformRoleCacheDao.class);
	private static final String KEY = "auth:role:platform:";
	private static final int TIME = 60 * 60 * 24 * 180;

	@Autowired
	private StringValueCache svc;

	public void setPlatformRole(RolePermissionVo rolePermission) {
		try {
			if (rolePermission == null) {
				return;
			}
			svc.set(KEY + rolePermission.getRoleid(), JSONUtils.toJSONString(rolePermission), TIME);
		} catch (Exception e) {
			LOGGER.error("setPlatformRole", e);
		}
	}

	public RolePermissionVo getPlatformRole(String roleId) {
		try {
			return JSONUtils.parseObject(svc.get(KEY + roleId), RolePermissionVo.class);
		} catch (Exception e) {
			LOGGER.error("getPlatformRole", e);
			return null;
		}
	}
}
