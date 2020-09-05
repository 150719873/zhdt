package com.dotop.smartwater.dependence.cache.api;

import com.dotop.smartwater.dependence.core.common.BaseCache;
import com.dotop.smartwater.dependence.core.global.BaseGlobalCommon;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**

 * @date 2019年5月8日
 * @description 缓存基类
 */
public abstract class AbstractBaseCache<K, V, H> extends BaseGlobalCommon implements BaseCache<K, V, H> {

    @Resource(name = "cacheRedisTemplate")
    protected RedisTemplate<String, String> cacheRedisTemplate;

    @Resource(name = "cacheOpsForValue")
    protected ValueOperations<String, String> cacheOpsForValue;

    @Resource(name = "cacheOpsForHash")
    protected HashOperations<String, String, String> cacheOpsForHash;

    @Resource(name = "cacheOpsForList")
    protected ListOperations<String, String> cacheOpsForList;
}
