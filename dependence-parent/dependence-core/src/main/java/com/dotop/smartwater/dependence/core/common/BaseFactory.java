package com.dotop.smartwater.dependence.core.common;

import java.util.ArrayList;
import java.util.List;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 基础类
 */
public interface BaseFactory<F extends BaseForm, V extends BaseVo> {

	public default V add(F f) throws FrameworkRuntimeException {
		return null;
	}

	public default V get(F f) throws FrameworkRuntimeException {
		return null;
	}

	public default Pagination<V> page(F f) throws FrameworkRuntimeException {
		return null;
	}

	public default List<V> list(F f) throws FrameworkRuntimeException {
		return new ArrayList<>();
	}

	public default V edit(F f) throws FrameworkRuntimeException {
		return null;
	}

	public default String del(F f) throws FrameworkRuntimeException {
		return null;
	}
}
