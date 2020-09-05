package com.dotop.smartwater.dependence.core.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * 基础类
 */
public interface BaseDao<D extends BaseDto, V extends BaseVo> {

	default void add(D d) throws DataAccessException {
	}

	default V get(D d) throws DataAccessException {
		return null;
	}

	default List<V> list(D d) throws DataAccessException {
		return new ArrayList<>();
	}

	default Integer listCount(D d) throws DataAccessException {
		return 0;
	}

	default Integer edit(D d) throws DataAccessException {
		return 0;
	}

	default Integer del(D d) throws DataAccessException {
		return 0;
	}

	default Boolean isExist(D d) throws DataAccessException {
		return Boolean.FALSE;
	}

	default void adds(List<D> ds) throws DataAccessException {
	}

	default void edits(List<D> ds) throws DataAccessException {
	}

	default void dels(List<D> ds) throws DataAccessException {
	}
}
