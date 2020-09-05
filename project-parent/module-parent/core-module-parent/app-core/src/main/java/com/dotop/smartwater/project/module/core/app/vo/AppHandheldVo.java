package com.dotop.smartwater.project.module.core.app.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AppHandheldVo extends BaseVo {

	private String code;

	private String msg;

	private Object data;

	public AppHandheldVo(String code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public AppHandheldVo() {
		super();
	}

}
