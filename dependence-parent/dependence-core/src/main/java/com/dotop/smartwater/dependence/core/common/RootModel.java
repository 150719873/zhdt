package com.dotop.smartwater.dependence.core.common;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础类
 */
@Getter
@Setter
public class RootModel {

	public static final int NOT_DEL = 0;
	public static final int DEL = 1;

	@JSONField(serialize = false)
	private String createBy;
	@JSONField(serialize = false)
	private Date createDate;
	@JSONField(serialize = false)
	private String lastBy;
	@JSONField(serialize = false)
	private Date lastDate;
	@JSONField(serialize = false)
	private int isDel = NOT_DEL;

}
