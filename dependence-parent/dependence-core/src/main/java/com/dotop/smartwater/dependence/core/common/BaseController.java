package com.dotop.smartwater.dependence.core.common;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础类
 */
public interface BaseController<F extends BaseForm> {

    default String resp(Object obj) {
        if (obj == null) {
            return resp();
        }
        return JSONUtils.toJSONString(obj);
    }

    default String resp() {
        return "{}";
    }

    default String resp(Object... objs) {
        Map<Object, Object> params = new HashMap<>(2);
        int step = 2;
        for (int i = 0; i < objs.length; i = i + step) {
            params.put(objs[i], objs[i + 1]);
        }
        return JSONUtils.toJSONString(params);
    }

    default String resp(String code, String msg, Object data) {
        Map<Object, Object> params = new HashMap<>(3);
        params.put("code", code);
        params.put("msg", msg);
        params.put("data", data);
        return JSONUtils.toJSONString(params);
    }

    default String resp(String code, String msg, Pagination<?> pagination) {
        Map<Object, Object> params = new HashMap<>(3);
        params.put("code", code);
        params.put("msg", msg);
        if (pagination != null) {
            params.put("data", pagination.getData());
            params.put("count", pagination.getTotalPageSize());
        } else {
            params.put("data", null);
        }
        return JSONUtils.toJSONString(params);
    }

    default String resp(int code, String msg, Pagination<?> pagination) {
        Map<Object, Object> params = new HashMap<>(3);
        params.put("code", code);
        params.put("msg", msg);
        if (pagination != null) {
            params.put("data", pagination.getData());
            params.put("count", pagination.getTotalPageSize());
        } else {
            params.put("data", null);
        }
        return JSONUtils.toJSONString(params);
    }

    // 新增
    default String add(F f) throws FrameworkRuntimeException {
        return resp();
    }

    // 详情
    default String get(F agrs) throws FrameworkRuntimeException {
        return resp();
    }

    // 分页
    default String page(F f) throws FrameworkRuntimeException {
        return resp();
    }

    // 列表
    default String list(F f) throws FrameworkRuntimeException {
        return resp();
    }

    // 编辑
    default String edit(F f) throws FrameworkRuntimeException {
        return resp();
    }

    // 删除
    default String del(F f) throws FrameworkRuntimeException {
        return resp();
    }

}
