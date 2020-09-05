package com.dotop.pipe.auth.cache.enterprise;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.dotop.pipe.auth.api.cache.enterprise.IEnterpriseCache;
import com.dotop.pipe.auth.core.constants.CacheKey;
import com.dotop.pipe.auth.core.vo.enterprise.EnterpriseVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;

@Component
public class EnterpriseCacheImpl extends IEnterpriseCache {// BaseCommon implements

	@Resource(name = "cacheRedisTemplate")
	private RedisTemplate<String, String> cacheRedisTemplate;

	@Resource(name = "cacheOpsForValue")
	private ValueOperations<String, String> cacheOpsForValue;

	@Override
	public EnterpriseVo get(String eid) throws FrameworkRuntimeException {
		String e = cacheOpsForValue.get(CacheKey.e(eid));
		if (StringUtils.isNotBlank(e)) {
			EnterpriseVo enterprise = JSONUtils.parseObject(e, EnterpriseVo.class);
			return enterprise;
		}
		return null;
	}

	@Override
	public void set(String enterpriseId, String eid) throws FrameworkRuntimeException {
		EnterpriseVo enterprise = new EnterpriseVo(enterpriseId, eid);
		cacheOpsForValue.set(CacheKey.e(eid), JSONUtils.toJSONString(enterprise), authEnterpriseExpire,
				TimeUnit.SECONDS);
	}

}
