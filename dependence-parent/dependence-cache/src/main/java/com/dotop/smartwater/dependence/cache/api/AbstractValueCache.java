package com.dotop.smartwater.dependence.cache.api;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**

 * @date 2019年5月8日
 * @description keyval缓存基类
 */
public abstract class AbstractValueCache<V> extends AbstractBaseCache<String, V, String> {

    @SuppressWarnings("unchecked")
    @Override
    public V get(String key, Class<V> clazz) {
        if (StringUtils.isNotBlank(key)) {
            String str = cacheOpsForValue.get(key);
            V v = null;
            if (StringUtils.isNotBlank(str)) {
                if (clazz.equals(String.class)) {
                    v = (V) str;
                } else {
                    v = JSONUtils.parseObject(str, clazz);
                }
            }
            return v;
        }
        return null;
    }

    @Override
    public <T> T getVo(String key, Class<T> clazz) {
        if (StringUtils.isNotBlank(key)) {
            String str = cacheOpsForValue.get(key);
            if (StringUtils.isNotBlank(str)) {
                return JSONUtils.parseObject(str, clazz);
            }
        }
        return null;
    }

    @Override
    public String set(String key, V v) {
        if (StringUtils.isNotBlank(key) && v != null) {
            if (v instanceof String) {
                cacheOpsForValue.set(key, (String) v, redisCacheCommonExpire, TimeUnit.SECONDS);
            } else {
                cacheOpsForValue.set(key, JSONUtils.toJSONString(v), redisCacheCommonExpire, TimeUnit.SECONDS);
            }
        }
        return null;
    }

    @Override
    public String set(String key, V v, long timeout) {
        if (StringUtils.isNotBlank(key) && v != null) {
            if (v instanceof String) {
                cacheOpsForValue.set(key, (String) v, timeout, TimeUnit.SECONDS);
            } else {
                cacheOpsForValue.set(key, JSONUtils.toJSONString(v), timeout, TimeUnit.SECONDS);
            }
        }
        return null;
    }

    @Override
    public String setVo(String key, Object v) {
        if (StringUtils.isNotBlank(key) && v != null) {
            if (v instanceof String) {
                cacheOpsForValue.set(key, (String) v, redisCacheCommonExpire, TimeUnit.SECONDS);
            } else {
                cacheOpsForValue.set(key, JSONUtils.toJSONString(v), redisCacheCommonExpire, TimeUnit.SECONDS);
            }
        }
        return null;
    }

    @Override
    public String setVo(String key, Object v, long timeout) {
        if (StringUtils.isNotBlank(key) && v != null) {
            if (v instanceof String) {
                cacheOpsForValue.set(key, (String) v, timeout, TimeUnit.SECONDS);
            } else {
                cacheOpsForValue.set(key, JSONUtils.toJSONString(v), timeout, TimeUnit.SECONDS);
            }
        }
        return null;
    }

    @Override
    public String del(String key) {
        if (StringUtils.isNotBlank(key)) {
            cacheRedisTemplate.delete(key);
        }
        return null;
    }

    @Override
    public String dels(List<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            cacheRedisTemplate.delete(keys);
        }
        return null;
    }

    @Override
    public void expire(String key, long timeout) {
        cacheRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public Long incr(String key, long timeout) {
        RedisConnectionFactory connectionFactory = cacheRedisTemplate.getConnectionFactory();
        if (connectionFactory != null) {
            RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, connectionFactory);
            Long increment = entityIdCounter.getAndIncrement();
            // 初始设置过期时间
            if (increment == 0 && timeout > 0) {
                entityIdCounter.expire(timeout, TimeUnit.SECONDS);
            }
            return increment;
        }
        return null;
    }

}
