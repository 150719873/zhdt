package com.dotop.smartwater.dependence.core.common;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础类
 */
public interface BaseCache<K, V, H> {

    default V get(K k) throws FrameworkRuntimeException {
        return null;
    }

    default V get(String key, Class<V> clazz) throws FrameworkRuntimeException {
        return null;
    }

    default <T> T getVo(String key, Class<T> clazz) throws FrameworkRuntimeException {
        return null;
    }

    default String set(K k, V v) throws FrameworkRuntimeException {
        return null;
    }

    default String set(K k, V v, long timeout) throws FrameworkRuntimeException {
        return null;
    }

    default String setVo(K k, Object v) throws FrameworkRuntimeException {
        return null;
    }

    default String setVo(K k, Object v, long timeout) throws FrameworkRuntimeException {
        return null;
    }

    default String del(K k) throws FrameworkRuntimeException {
        return null;
    }

    default String dels(List<K> ks) throws FrameworkRuntimeException {
        return null;
    }

    default V hashGet(H h, K k) throws FrameworkRuntimeException {
        return null;
    }

    default V hashGet(H h, K k, Class<V> clazz) throws FrameworkRuntimeException {
        return null;
    }

    default List<V> hashMGet(H h, K k) throws FrameworkRuntimeException {
        return new ArrayList<>();
    }

    default String hashSet(H h, K k, V v) throws FrameworkRuntimeException {
        return null;
    }

    default String hashSet(H h, K k, V v, long timeout) throws FrameworkRuntimeException {
        return null;
    }

    default String hashDel(H h) throws FrameworkRuntimeException {
        return null;
    }

    default String hashDels(H h, List<K> ks) throws FrameworkRuntimeException {
        return null;
    }

    default void expire(K k, long timeout) throws FrameworkRuntimeException {
    }

    default Long incr(K k, long timeout) throws FrameworkRuntimeException {
        return null;
    }

    default String leftPush(K k, V v) throws FrameworkRuntimeException {
        return null;
    }

    default String leftPush(K k, List<V> vs) throws FrameworkRuntimeException {
        return null;
    }

    default V leftPop(K k) throws FrameworkRuntimeException {
        return null;
    }

    default V leftPop(K k, long timeout) throws FrameworkRuntimeException {
        return null;
    }

    default V leftPop(K k, Class<V> clazz, long timeout) throws FrameworkRuntimeException {
        return null;
    }

    default String rightPush(K k, V v) throws FrameworkRuntimeException {
        return null;
    }

    default String rightPush(K k, V... v) throws FrameworkRuntimeException {
        return null;
    }

    default String rightPush(K k, List<V> vs) throws FrameworkRuntimeException {
        return null;
    }

    default V rightPop(K k) throws FrameworkRuntimeException {
        return null;
    }

    default V rightPop(K k, long timeout) throws FrameworkRuntimeException {
        return null;
    }

    default V rightPop(K k, Class<V> clazz, long timeout) throws FrameworkRuntimeException {
        return null;
    }

    default List<V> lrange(K k, long start, long end) throws FrameworkRuntimeException {
        return null;
    }

    default List<V> lrange(K k, Class<V> clazz, long start, long end) throws FrameworkRuntimeException {
        return null;
    }
}
