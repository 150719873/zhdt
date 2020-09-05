package com.dotop.pipe.auth.api.cache.enterprise;

import com.dotop.pipe.auth.core.vo.enterprise.EnterpriseVo;
import com.dotop.smartwater.dependence.cache.api.AbstractBaseCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

// 企业
public abstract class IEnterpriseCache extends AbstractBaseCache<String, EnterpriseVo, String> {

	// 获取企业
	public abstract EnterpriseVo get(String eid) throws FrameworkRuntimeException;

	// 新增绑定企业
	public abstract void set(String enterpriseId, String eid) throws FrameworkRuntimeException;

}
