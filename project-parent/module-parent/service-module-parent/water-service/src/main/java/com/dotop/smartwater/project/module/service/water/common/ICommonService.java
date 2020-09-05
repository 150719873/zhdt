package com.dotop.smartwater.project.module.service.water.common;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface ICommonService {

	/**
	 * 提供功能sql查询直接操作方法
	 */
	List<Map<String, Object>> list(String sql, Map<String, String> sqlParams);

	/**
	 * 提供功能sql查询直接操作方法
	 */
	Pagination<Map<String, Object>> page(String sql, Map<String, String> sqlParams, Integer page, Integer pageCount);
}
