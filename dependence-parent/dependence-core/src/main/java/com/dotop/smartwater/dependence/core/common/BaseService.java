package com.dotop.smartwater.dependence.core.common;

import java.util.ArrayList;
import java.util.List;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 基础类
 */
public interface BaseService<B extends BaseBo, V extends BaseVo> {

	public default V add(B b) throws FrameworkRuntimeException {
		return null;
	}

	public default V get(B b) throws FrameworkRuntimeException {
		return null;
	}

	public default V getByCode(B b) throws FrameworkRuntimeException {
		return null;
	}

	public default Pagination<V> page(B b) throws FrameworkRuntimeException {
		return null;
	}

	public default List<V> list(B b) throws FrameworkRuntimeException {
		return new ArrayList<>();
	}

	public default V edit(B b) throws FrameworkRuntimeException {
		return null;
	}

	public default String del(B b) throws FrameworkRuntimeException {
		return null;
	}

	public default boolean isExist(B b) throws FrameworkRuntimeException {
		return false;
	}

	public default void adds(List<B> bs) throws FrameworkRuntimeException {
	}

	public default void edits(List<B> bs) throws FrameworkRuntimeException {
	}

	public default void dels(List<B> bs) throws FrameworkRuntimeException {
	}
}
