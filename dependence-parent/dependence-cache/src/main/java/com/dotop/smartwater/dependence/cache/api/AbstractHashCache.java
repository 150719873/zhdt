package com.dotop.smartwater.dependence.cache.api;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**

 * @date 2019年5月8日
 * @description hash缓存基类
 */
public abstract class AbstractHashCache<V> extends AbstractBaseCache<String, V, String> {

    @Override
    public String hashSet(String hash, String key, V v) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(hash) && StringUtils.isNotBlank(key) && v != null) {
            if (v instanceof String) {
                cacheOpsForHash.put(hash, key, (String) v);
            } else {
                cacheOpsForHash.put(hash, key, JSONUtils.toJSONString(v));
            }
            cacheRedisTemplate.expire(hash, redisCacheCommonExpire, TimeUnit.SECONDS);
        }
        return null;
    }

    @Override
    public String hashSet(String hash, String key, V v, long timeout) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(hash) && StringUtils.isNotBlank(key) && v != null) {
            if (v instanceof String) {
                cacheOpsForHash.put(hash, key, (String) v);
            } else {
                cacheOpsForHash.put(hash, key, JSONUtils.toJSONString(v));
            }
            cacheRedisTemplate.expire(hash, timeout, TimeUnit.SECONDS);
        }
        return null;
    }

    @Override
    public V hashGet(String hash, String key, Class<V> clazz) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(hash) && StringUtils.isNotBlank(key)) {
            String str = cacheOpsForHash.get(hash, key);
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
    public String hashDel(String hash) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(hash)) {
            cacheRedisTemplate.delete(hash);
        }
        return null;
    }

    @Override
    public String hashDels(String hash, List<String> keys) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(hash) && keys != null && !keys.isEmpty()) {
            cacheOpsForHash.delete(hash, keys.toArray());
        }
        return null;
    }

    @Override
    public void expire(String hash, long timeout) throws FrameworkRuntimeException {
        cacheRedisTemplate.expire(hash, timeout, TimeUnit.SECONDS);
    }
}
