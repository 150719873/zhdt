package com.dotop.smartwater.dependence.cache.api;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**

 * @date 2019年6月24日
 * @description list缓存基类
 */
public abstract class AbstractListCache<V> extends AbstractBaseCache<String, V, String> {

    @Override
    public String leftPush(String key, V v) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(key) && v != null) {
            if (v instanceof String) {
                cacheOpsForList.leftPush(key, (String) v);
            } else {
                cacheOpsForList.leftPush(key, JSONUtils.toJSONString(v));
            }
        }
        return null;
    }

    @Override
    public String rightPush(String key, V v) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(key) && v != null) {
            if (v instanceof String) {
                cacheOpsForList.rightPush(key, (String) v);
            } else {
                cacheOpsForList.rightPush(key, JSONUtils.toJSONString(v));
            }
        }
        return null;
    }

    @Override
    public String leftPush(String key, List<V> vs) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(key) && !vs.isEmpty()) {
            List<String> list = new ArrayList<>();
            for (V v : vs) {
                if (v.equals(String.class)) {
                    list.add((String) v);
                } else {
                    list.add(JSONUtils.toJSONString(v));
                }
            }
            cacheOpsForList.leftPushAll(key, list);
        }
        return null;
    }

    @Override
    public String rightPush(String key, List<V> vs) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(key) && !vs.isEmpty()) {
            List<String> list = new ArrayList<>();
            for (V v : vs) {
                if (v.equals(String.class)) {
                    list.add((String) v);
                } else {
                    list.add(JSONUtils.toJSONString(v));
                }
            }
            cacheOpsForList.rightPushAll(key, list);
        }
        return null;
    }

    @Override
    public V rightPop(String key, Class<V> clazz, long timeout) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(key)) {
            String str = cacheOpsForList.rightPop(key, timeout, TimeUnit.SECONDS);
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
    public List<V> lrange(String key, Class<V> clazz, long start, long end) throws FrameworkRuntimeException {
        if (StringUtils.isNotBlank(key)) {
            List<String> range = cacheOpsForList.range(key, start, end);
            if (range != null && !range.isEmpty()) {
                List<V> list = new ArrayList<>();
                for (String str : range) {
                    V v = null;
                    if (StringUtils.isNotBlank(str)) {
                        if (clazz.equals(String.class)) {
                            v = (V) str;
                        } else {
                            v = JSONUtils.parseObject(str, clazz);
                        }
                    }
                    list.add(v);
                }
                return list;
            }
        }
        return new ArrayList<>();
    }
}
