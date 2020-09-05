package com.dotop.smartwater.project.auth.cache;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@Component
public class AreaNodeMapCacheDao {

	private static final Logger LOGGER = LogManager.getLogger(AreaNodeMapCacheDao.class);

	private static final String KEY = "auth:areaNodeMap:";
	private static final int TIME = 60 * 60 * 24;

	@Autowired
	private StringValueCache svc;

	public void setAreaNodeMap(String mKey, Map<String, AreaNodeVo> map) {
		try {
			if (map == null) {
				return;
			}
			svc.set(KEY + mKey, JSONUtils.toJSONString(map), TIME);
		} catch (Exception e) {
			LOGGER.error("setAreaNodeMap", e);
		}
	}

	public Map<String, AreaNodeVo> getAreaNodeMap(String mKey) {
		try {
			return JSONUtils.parseObject(svc.get(KEY + mKey), String.class, AreaNodeVo.class);
		} catch (Exception e) {
			LOGGER.error("getAreaNodeMap", e);
			return null;
		}
	}

	public void delAreaNodeMap(String mKey) {
		try {
			svc.del(KEY + mKey);
		} catch (Exception e) {
			LOGGER.error("delAreaNodeMap", e);
		}
	}
}
