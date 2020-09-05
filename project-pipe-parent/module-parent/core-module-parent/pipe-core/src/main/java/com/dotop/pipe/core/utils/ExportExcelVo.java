package com.dotop.pipe.core.utils;

import lombok.Data;

@Data
public class ExportExcelVo {

	private String category;

	private String categoryName;

	private String[] titles;

	public ExportExcelVo() {
		super();
	}

	public ExportExcelVo(String category, String categoryName, String[] titles) {
		super();
		this.category = category;
		this.categoryName = categoryName;
		this.titles = titles;
	}

}
