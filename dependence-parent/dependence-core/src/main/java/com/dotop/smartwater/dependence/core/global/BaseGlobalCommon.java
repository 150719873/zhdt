package com.dotop.smartwater.dependence.core.global;

import org.springframework.beans.factory.annotation.Value;

/**
 * 

 * @date 2019年5月8日
 * @description 全局变量设定
 */
public abstract class BaseGlobalCommon {

	@Value("${redis.cache.common.expire:86400}")
	protected long redisCacheCommonExpire;

	@Value("${auth.cas.expire:600}")
	protected long authCasExpire;

	@Value("${auth.enterprise.expire:600}")
	protected long authEnterpriseExpire;

	@Value("${device.property.expire:600}")
	protected long devicePropertyExpire;

	@Value("${rabbitmq.send.expiration:3600}")
	protected Integer rabbitmqSendExpire;
}
