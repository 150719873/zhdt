package com.dotop.smartwater.dependence.core.pagination;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 分页对象
 */
@Data
public class Pagination<T> {

	/**
	 * 默认单页显示条数
	 */
	public static final long DEFAULT_PAGE_SIZE = 15;
	/**
	 * 默认当前页号
	 */
	public static final long DEFAULT_PAGE_NO = 1;

	/**
	 * long型默认值
	 */
	public static final long ERROR_LONG = -1;

	/**
	 * 数据
	 */
	private List<T> data;

	/**
	 * 单页显示条数
	 */
	private long pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 当前页号
	 */
	private long pageNo = DEFAULT_PAGE_NO;

	/**
	 * 总显示条数
	 */
	private long totalPageSize = ERROR_LONG;

	/**
	 * 总页数
	 */
	@JSONField(serialize = false)
	private long totalPageNo = ERROR_LONG;

	public Pagination() {
		super();
	}

	public Pagination(int pageSize, int pageNo) {
		super();
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}

	public Pagination(long pageSize, long pageNo) {
		super();
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}

	public Pagination(long pageNo, long pageSize, List<T> data, long totalPageSize) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.data = data;
		this.totalPageSize = totalPageSize;
	}

	/**
	 * 是否第一页
	 */
	@JSONField(serialize = false)
	public boolean isShowFirst() {
		return pageNo == DEFAULT_PAGE_NO;
	}

	/**
	 * 是否最后一页
	 */
	@JSONField(serialize = false)
	public boolean isShowLast() {
		return pageNo == getTotalPageNo();
	}

	/**
	 * 是否显示下一页
	 */
	@JSONField(serialize = false)
	public boolean isShowNext() {
		return pageNo != getTotalPageNo();
	}

	/**
	 * 是否显示前一页
	 */
	@JSONField(serialize = false)
	public boolean isShowPrevious() {
		return pageNo != DEFAULT_PAGE_NO;
	}

}
